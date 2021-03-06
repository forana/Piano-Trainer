package crescendo.base.parsing;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import crescendo.base.song.Creator;
import crescendo.base.song.Note;
import crescendo.base.song.SongModel;
import crescendo.base.song.TimeSignature;
import crescendo.base.song.Track;

public class MusicXmlParser implements SongFileParser{
	private String movementTitle;
	private double movementNumber;
	private String workTitle;
	private int workNumber;
	private List<Creator> creators;
	private String copyright;
	private Map<String,Track> tracks;
	private int bpm;
	private TimeSignature timeSignature;
	private int keySignature;
	
	private int currentMeasure = -1;	//Counts the current measure, useful for determining if directives are global or not
	private int currentDynamic = 90;	//The midi dynamic for the notes currently being parsed. MusicXML default is 90 (about forte)
	private int currentDivision = -1;	//The current count of number of divisions per quarter note
	private Track currentTrack;
	
	
	//All of the possible named notes in midi
	private static String[] midiNoteArray = new String[]{"c","db","d","eb","e","f","gb","g","ab","a","bb","b"};
	
	/**
	 * Constructs a MusicXmlParser
	 * initializes global variables.
	 */
	public MusicXmlParser(){
		currentTrack = null;
		tracks = new HashMap<String,Track>();
		creators = new ArrayList<Creator>();
		bpm = 120; // default
		timeSignature = null;
		keySignature = 0;
	}
	
	/**
	 * Basic function to parse a file into a song function.
	 * @param file - MusicXML file to parse
	 * @return SongModel - model which contains the data contained in the MusicXML file
	 */
	@Override
	public SongModel parse(File file) throws IOException {
		ZipFile zf=null;
		boolean validating = false; //Semaphore for the DocumentBuilderFactory
		DocumentBuilderFactory factory  = DocumentBuilderFactory.newInstance();
		factory.setValidating(validating);
		// fix for no-internet issue, part 1 - forana
		factory.setSchema(null);
		// end part 1
		DocumentBuilder builder;
		try
		{
			builder = factory.newDocumentBuilder();
			// fix for no-internet issue, part 2 - forana
			builder.setEntityResolver(new EntityResolver() {
				public InputSource resolveEntity(String publicId,String systemId) throws SAXException,IOException {
					return new InputSource(new StringReader(""));
				}
			});
			// end part 2
		}
		catch (ParserConfigurationException e)
		{
			// pipe the message through as an IOException
			throw new IOException(e.getMessage());
		}
		
		InputStream songStream; // this will be the stream that will be parsed as the song
		if (file.getName().toLowerCase().endsWith(".mxl"))
		{
			System.out.println("I am a zip");
			zf=new ZipFile(file);
			ZipEntry containerEntry=zf.getEntry("META-INF/container.xml");
			if (containerEntry==null)
			{
				throw new IOException("No container entry found in compressed musicXML file.");
			}
			else
			{
				try
				{
					Document containerDoc=builder.parse(zf.getInputStream(containerEntry));
					String rootFileName=null;
					NodeList rootfiles=containerDoc.getElementsByTagName("rootfile");
					for (int i=0; i<rootfiles.getLength(); i++)
					{
						Node node=rootfiles.item(i);
						Node fullpath=node.getAttributes().getNamedItem("full-path");
						if (fullpath!=null)
						{
							Node mediaType=node.getAttributes().getNamedItem("media-type");
							if (mediaType==null || mediaType.getTextContent().equals("application/vnd.recordare.musicxml+xml"))
							{
								rootFileName=fullpath.getTextContent();
							}
						}
					}
					if (rootFileName==null)
					{
						throw new IOException("Could not locate root file in compressed musicXML.");
					}
					else
					{
						songStream=zf.getInputStream(zf.getEntry(rootFileName));
					}
				}
				catch (SAXException e)
				{
					throw new IOException(e.getMessage());
				}
			}
			
		}
		else
		{
			songStream=new FileInputStream(file);
		}
		
		Document document = null;
		try {
			document = builder.parse(songStream);
		} catch (SAXException e) {
			e.printStackTrace();
		}
		for(int i=0;i<document.getChildNodes().getLength();i++)
		{
			parseNode(document.getChildNodes().item(i));
		}
		if (zf!=null)
		{
			zf.close();
		}
		return new SongModel(new ArrayList<Track>(tracks.values()), workTitle, creators, copyright, bpm, timeSignature,keySignature);
	}
	
	/**
	 * Catch-all helper function for recursively parsing nodes in the MusicXML DOM.
	 * Currently parses:
	 * 	* 	movement number
	 * 	*	creators
	 *  *	copyright information
	 *  *	time signature
	 *  *	tempo
	 *  *	dynamic
	 * @param node - XML DOM node to parse
	 */
	private void parseNode(Node node){
		String nodeName = node.getNodeName();
		boolean parseChildren=true;	//determines if the children of the current node should be recursed upon

		if(nodeName.equals("score-part")){
			tracks.put(node.getAttributes().getNamedItem("id").getNodeValue(), parseTrackMeta(node));
			parseChildren = false;
		}else if(nodeName.equals("work")){
			parseWork(node);
			parseChildren = false;
		}else if(nodeName.equals("movement-number")){
			movementNumber = node.getTextContent()==""?0:Integer.parseInt(node.getTextContent());
		}else if(nodeName.equals("movement-title")){
			movementTitle = node.getTextContent();
		}else if(nodeName.equals("creator")){
			creators.add(new Creator(node.getTextContent(), node.getAttributes().getNamedItem("type").getNodeValue()));
		}else if(nodeName.equals("rights")){
			copyright = node.getTextContent();
		}else if(nodeName.equals("part")){
			currentTrack = tracks.get(node.getAttributes().getNamedItem("id").getNodeValue());
		}else if(nodeName.equals("measure")){
			currentMeasure = Integer.parseInt(node.getAttributes().getNamedItem("number").getNodeValue());
		}else if(nodeName.equals("divisions")){
			currentDivision = node.getTextContent()==""?0:Integer.parseInt(node.getTextContent());
		}else if(nodeName.equals("key")){
			//TODO handle key signature
			if(currentMeasure==1){
				//Song-wide key signature
			}else{
				//non-playable note with a modulation modifier in currentTrack
			}
		}else if(nodeName.equals("time")){
			int beatsPerMeasure = -1;
			int beatNote = -1;
			for(int i=0;i<node.getChildNodes().getLength();i++){
				if(node.getChildNodes().item(i).getNodeName().equals("beats")){
					beatsPerMeasure = node.getChildNodes().item(i).getTextContent()==""?0:Integer.parseInt(node.getChildNodes().item(i).getTextContent());
				}else if(node.getChildNodes().item(i).getNodeName().equals("beat-type")){
					beatNote = Integer.parseInt(node.getChildNodes().item(i).getTextContent());
				}
			}
			TimeSignature ts = new TimeSignature(beatsPerMeasure,beatNote);
			if(currentMeasure==1){
				timeSignature = ts;
			}else{
				//TODO non-playable note with a time signature modifier in the currentTrack
			}
			parseChildren = false;
		}else if(nodeName.equals("sound")){
			if(node.getAttributes().getNamedItem("tempo")!=null){
				if(currentMeasure==1){
					bpm = Integer.parseInt(node.getAttributes().getNamedItem("tempo").getNodeValue());
				}else{
					//TODO non-playable note with a tempo change modifier in the current track
				}
			}
			if(node.getAttributes().getNamedItem("dynamics")!=null){
				currentDynamic = Integer.parseInt(node.getAttributes().getNamedItem("dynamics").getNodeValue());
			}
		}else if(nodeName.equals("note")){
			currentTrack.addNote(parseNote(node,false,0,0));
		}
		
		
		if(node.hasChildNodes()&&parseChildren){
			for(int i=0;i<node.getChildNodes().getLength();i++)
			{
				parseNode(node.getChildNodes().item(i));
			}
		}
	}
	
	/**
	 * Helper function for parsing out track information
	 * Currently only supports track name and midi instrument. It currently uses the last midi instrument defined for a given track.
	 * @param node - score-part node
	 * @return - a fully constructed Track object
	 */
	private Track parseTrackMeta(Node node){
		String trackName = "";
		int instrument = 1;
		if(!node.getNodeName().equals("score-part")){
			throw new IllegalArgumentException("Node must be of type score-part");
		}
		if(node.hasChildNodes()){
			for(int i=0;i<node.getChildNodes().getLength();i++)
			{
				Node cNode = node.getChildNodes().item(i);
				if(cNode.getNodeName().equals("midi-instrument")){
					for(int j=0;j<cNode.getChildNodes().getLength();j++){
						if(cNode.getChildNodes().item(j).getNodeName().equals("midi-channel")){
							instrument = Integer.parseInt(cNode.getChildNodes().item(j).getTextContent());
						}
					}
				}
				else if(cNode.getNodeName().equals("part-name")){
					trackName = cNode.getTextContent();
				}
			}
		}
		return new Track(trackName,instrument);
	}

	private Note parseNote(Node node, boolean isRest, double duration, int pitch){
		Note retValue = null;
		if(node.getNodeName().equals("pitch")){
			char note = ' ';
			int alter = 0;
			int octave = 0;
			for(int i=0;i<node.getChildNodes().getLength();i++)
			{
				if(node.getChildNodes().item(i).getNodeName().equals("step")){
					note = node.getChildNodes().item(i).getTextContent()==""?0:node.getChildNodes().item(i).getTextContent().toLowerCase().charAt(0);
				}else if(node.getChildNodes().item(i).getNodeName().equals("alter")){
					alter = node.getTextContent()==""?0:Integer.parseInt(node.getChildNodes().item(i).getTextContent());
				}else if(node.getChildNodes().item(i).getNodeName().equals("octave")){
					octave = node.getTextContent()==""?0:Integer.parseInt(node.getChildNodes().item(i).getTextContent());
				}
			}
			pitch = noteToMidi(note,alter,octave);
		}else if(node.getNodeName().equals("rest")){
			isRest = true;
		}else if(node.getNodeName().equals("duration")){
			duration =  node.getTextContent()==""?0:Double.parseDouble(node.getTextContent());
		}else if(node.getNodeName().equals("voice")){
			if(Integer.parseInt(node.getTextContent())!=currentTrack.getVoice()){
				//TODO non-playable note with a voice change modifier in the current track
			}
		}
		
		
		
		if(!node.hasChildNodes() && node.getNextSibling()==null){
			retValue = new Note((isRest)?1:pitch , (double)duration/(double)currentDivision , (isRest)?0:currentDynamic , currentTrack);
		}else if(node.getNodeName().equals("note")){
			retValue = parseNote(node.getFirstChild(),isRest,duration,pitch);
		}else{			
			retValue = parseNote(node.getNextSibling(),isRest,duration,pitch);
		}
		return retValue;
	}
	
	private void parseWork(Node node){
		if(node.hasChildNodes()){
			for(int i=0;i<node.getChildNodes().getLength();i++)
			{
				Node cNode = node.getChildNodes().item(i);
				if(cNode.getNodeName().equals("work-title")){
					workTitle = cNode.getTextContent();
				}else if(cNode.getNodeName().equals("work-number")){
					workNumber = cNode.getTextContent()==""?0:Integer.parseInt(cNode.getTextContent());
				}
			}
		}
	}

	private static int noteToMidi(char note, int alter, int octave){
		String noteValue = String.valueOf(note);
		int location = -1;
		for(int i=0;i<midiNoteArray.length;i++){
			if(noteValue.equals(midiNoteArray[i]))
				location = i;
		}
		location+=alter;
		if(location<0)
			location+=12;
		location+=(12)*(octave+1);
		return location;
	}
}
