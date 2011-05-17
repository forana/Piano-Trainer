package crescendo.base.profile;

import java.util.ArrayList;
import java.util.HashMap;

import junit.framework.TestCase;

/**
 * TestProfileManager
 * 
 * This UnitTest contains the functionality testing the capabilities of the
 * ProfileManager class to verify that the class works as expected.
 * 
 * @author groszc
 * 
 */
public class TestProfileManager extends TestCase {

	/**
	 * tearDown
	 * 
	 * remove all profiles from the ProfileManager
	 */
	public void tearDown() {
		ProfileManager manager = ProfileManager.getInstance();

		while (manager.getProfiles().size() > 0) {
			manager.removeProfile(manager.getProfiles().get(0));
		}
	}

	/**
	 * testAddProfile
	 * 
	 * Tests addProfile method of ProfileManager
	 */
	public void testAddProfile() {
		ProfileManager manager = ProfileManager.getInstance();

		ArrayList<Profile> profiles = new ArrayList<Profile>();

		for (int i = 0; i < 10; i++) {
			Profile newProfile = new Profile("Profile " + Math.random());
			profiles.add(newProfile);
			manager.addProfile(newProfile);

			// make sure we have all of the added profiles
			assertTrue(manager.getProfiles().containsAll(profiles));
		}
	}

	/**
	 * testRemoveProfile
	 * 
	 * Tests removeProfile method of ProfileManager
	 */
	public void testRemoveProfile() {
		ProfileManager manager = ProfileManager.getInstance();

		ArrayList<Profile> profiles = new ArrayList<Profile>();

		for (int i = 0; i < 10; i++) {
			Profile newProfile = new Profile("Profile " + Math.random());
			profiles.add(newProfile);
			manager.addProfile(newProfile);

			// make sure we have all of the added profiles
			assertTrue(manager.getProfiles().containsAll(profiles));
		}

		// remove random profiles and verify that it was removed
		// while still containing the profiles it should
		for (int i = 0; i < 10; i++) {
			int toRemove = (int) Math.round(Math.random() * (9 - i));
			Profile remove = profiles.remove(toRemove);
			manager.removeProfile(remove);

			// make sure we have all of the profiles it should
			assertTrue(manager.getProfiles().containsAll(profiles));

			// make sure we DON'T have the removed profile
			assertFalse(manager.getProfiles().contains(remove));

			// make sure they have the same number of profiles
			assertEquals(manager.getProfiles().size(), profiles.size());
		}

	}

	/**
	 * testSwitchProfile
	 * 
	 * Tests switchProfile method of ProfileManager
	 */
	public void testSwitchProfile() {
		ProfileManager manager = ProfileManager.getInstance();

		ArrayList<Profile> profiles = new ArrayList<Profile>();

		// add some profiles
		for (int i = 0; i < 10; i++) {
			Profile newProfile = new Profile("Profile " + Math.random());
			profiles.add(newProfile);
			manager.addProfile(newProfile);

			// make sure we have all of the added profiles
			assertTrue(manager.getProfiles().containsAll(profiles));
		}

		// switch some profiles, emulate is and verify result
		for (int i = 0; i < 10; i++) {
			Profile active = manager.getActiveProfile();
			Profile swap = manager.getProfiles().get(i);

			manager.switchProfile(swap);

			// switching involves adding the active profile to the list
			profiles.add(active);
			// and removing the selected one (to become the active profile)
			profiles.remove(swap);

			// make sure we have all of the profiles we should
			assertTrue(manager.getProfiles().containsAll(profiles));

			// make the active profile was set correctly
			assertEquals(manager.getActiveProfile(), swap);
		}
	}

	/**
	 * testGetProfileByName
	 * 
	 * Tests getProfileByName method of ProfileManager
	 */
	public void testGetProfileByName() {
		ProfileManager manager = ProfileManager.getInstance();

		// compare the getProfileByName to a map between the name and the
		// profile
		HashMap<String, Profile> profilestr = new HashMap<String, Profile>();

		Profile toAdd;

		// add some profiles
		toAdd = new Profile("Profile 1");
		manager.addProfile(toAdd);
		profilestr.put("Profile 1", toAdd);

		toAdd = new Profile("Profile 2");
		manager.addProfile(toAdd);
		profilestr.put("Profile 2", toAdd);

		toAdd = new Profile("Profile 3");
		manager.addProfile(toAdd);
		profilestr.put("Profile 3", toAdd);

		toAdd = new Profile("Profile 4");
		manager.addProfile(toAdd);
		profilestr.put("Profile 4", toAdd);

		// make sure the the names correctly correspond with the profile
		assertEquals(manager.getProfileByName("Profile 1"),
				profilestr.get("Profile 1"));
		assertEquals(manager.getProfileByName("Profile 2"),
				profilestr.get("Profile 2"));
		assertEquals(manager.getProfileByName("Profile 3"),
				profilestr.get("Profile 3"));
		assertEquals(manager.getProfileByName("Profile 4"),
				profilestr.get("Profile 4"));
	}

	/**
	 * testSerializable
	 * 
	 * Tests save/loadFromFile method of ProfileManager
	 */
	public void testSerializable() {
		ProfileManager manager = ProfileManager.getInstance();

		ArrayList<Profile> profiles = new ArrayList<Profile>();
		Profile defaultActive = manager.getActiveProfile();

		for (int i = 0; i < 10; i++) {
			Profile newProfile = new Profile("Profile " + i);
			profiles.add(newProfile);
			manager.addProfile(newProfile);

			// make sure we have all of the added profiles
			assertTrue(manager.getProfiles().containsAll(profiles));
		}

		manager.saveToFile("testSerializableProfileManager");

		// mess up the ProfileManager away from the original
		manager.switchProfile(manager.getProfiles().get(1));
		manager.getProfiles().clear();

		manager.loadFromFile("testSerializableProfileManager");

		// now make sure the state was as it was when it was written to a file
		for (int i = 0; i < 10; i++) {
			// System.out.println(manager.getProfiles().get(i).getName() +
			// "    " + profiles.get(i).getName());
			assertTrue(manager.getProfiles().get(i).compareTo(profiles.get(i)) == 0);
		}

		assertTrue(manager.getActiveProfile().compareTo(defaultActive) == 0);
	}
}
