/*
 * Copyright (C) 2011 Lalit Pant <pant.lalit@gmail.com>
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public final class SaveAs implements ActionListener {

    public File chooseFile(String desc, String ext) {
        CodeEditorTopComponent cetc = CodeEditorTopComponent.findInstance();

        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                desc, ext);
        chooser.setFileFilter(filter);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        String loadDir = cetc.getLastLoadStoreDir();
        if (loadDir != null && loadDir != "") {
            File dir = new File(loadDir);
            if (dir.exists() && dir.isDirectory()) {
                chooser.setCurrentDirectory(dir);
            }
        }

        int returnVal = chooser.showSaveDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            cetc.setLastLoadStoreDir(chooser.getSelectedFile().getParent());
            File selectedFile = chooser.getSelectedFile();
            if (!selectedFile.getName().endsWith("." + ext)) {
                selectedFile = new File(selectedFile.getAbsolutePath() + "." + ext);
            }
            return selectedFile;
        } else {
            return null;
        }
    }

    public void actionPerformed(ActionEvent e) {

        File selectedFile = chooseFile("Kojo Files", "kojo");

        if (selectedFile != null) {
            try {
                CodeExecutionSupport ces = (CodeExecutionSupport) CodeExecutionSupport.instance();
                ces.saveAs(selectedFile);
                ces.openFileWithoutClose(selectedFile);
            } catch (IllegalArgumentException ex) {
                // user said no to over-writing selected file
                // try again
                actionPerformed(e);
            }
            catch (RuntimeException ex) {
                // user cancelled save
            }
        }
    }
}
