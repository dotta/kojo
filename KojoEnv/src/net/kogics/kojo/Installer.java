/*
 * Copyright (C) 2009 Lalit Pant <pant.lalit@gmail.com>
 *
 * The contents of this file are subject to the GNU General Public License
 * Version 3 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.gnu.org/copyleft/gpl.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 */
package net.kogics.kojo;

import javax.swing.SwingUtilities;
import net.kogics.kojo.mathworld.GeoGebraCanvas;
import net.kogics.kojo.story.StoryTeller;
import org.openide.modules.ModuleInstall;

/**
 * Manages a module's lifecycle. Remember that an installer is optional and
 * often not needed at all.
 */
public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        System.setProperty("actors.corePoolSize", "8");
        System.setProperty("actors.maxPoolSize", "512");
        try {
            // Init canvases in swing thread
            SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    GeoGebraCanvas.initedInstance((KojoCtx) KojoCtx.instance());
                    SpriteCanvas.initedInstance((KojoCtx) KojoCtx.instance());
                    StoryTeller.initedInstance((KojoCtx) KojoCtx.instance());
                }
            });
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public boolean closing() {
        ((GeoGebraCanvas) GeoGebraCanvas.instance()).ensureWorkSaved();
        try {
            ((CodeExecutionSupport) CodeExecutionSupport.instance()).closeFileIfOpen();
            return true;
        }
        catch (RuntimeException e) {
            return false;
        }
    }
}
