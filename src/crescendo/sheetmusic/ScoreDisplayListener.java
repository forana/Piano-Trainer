package crescendo.sheetmusic;

import crescendo.base.FlowController;

public class ScoreDisplayListener implements FlowController {
	private SheetMusic module;
	
	public ScoreDisplayListener(SheetMusic module) {
		this.module=module;
	}
	
	public void pause() {}
	public void resume() {}
	public void stop() {}
	public void suspend() {}
	
	public void songEnd() {
		module.showScore();
	}
}
