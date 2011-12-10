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
package net.kogics.kojo

import javax.swing._
import java.awt._
import java.awt.event._
import javax.swing.border._

class HistoryListModel(myList: JList) extends AbstractListModel {
  val commandHistory = CommandHistory.instance

  def getSize = commandHistory.size + 1

  def getElementAt(idx: Int) = {
    if (idx == commandHistory.size) ""
    else commandHistory(idx)
  }
  
  commandHistory.setListener(new HistoryListener {
      def itemAdded {
        fireIntervalAdded(HistoryListModel.this,commandHistory.size-1, commandHistory.size-1)
        myList.setSelectedIndex(getSize-1)
      }

      def selectionChanged(n: Int) {
          myList.setSelectedIndex(n)
      }

      def ensureVisible(n: Int) {
        myList.ensureIndexIsVisible(n)
      }
    })

  def addStar(idx: Int) = commandHistory.addStar(idx)
  def removeStar(idx: Int) = commandHistory.removeStar(idx)
  def isStarred(idx: Int) = commandHistory.isStarred(idx)
}

class HistoryPopupMenu(myList: JList) extends JPopupMenu {
  val starItem = new JMenuItem("Star")
  starItem.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent) {
        myList.getModel.asInstanceOf[HistoryListModel].addStar(myList.getSelectedIndex)
        myList.repaint()
      }
    })
  add(starItem)
  val unstarItem = new JMenuItem("UnStar")
  unstarItem.addActionListener(new ActionListener {
      override def actionPerformed(e: ActionEvent) {
        myList.getModel.asInstanceOf[HistoryListModel].removeStar(myList.getSelectedIndex)
        myList.repaint()
      }
    })
  add(unstarItem)
}

class HistoryCellRenderer extends DefaultListCellRenderer {

  val star = net.kogics.kojo.util.Utils.loadIcon("/images/star.png")
  val outsideBorder = BorderFactory.createLineBorder(new Color(240, 240, 240), 1);
  val insideBorder = new EmptyBorder(3, 3, 2, 1)
  val border = new CompoundBorder(outsideBorder, insideBorder)

  override def getListCellRendererComponent(list: JList,
                                            value: Object,
                                            index: Int,
                                            isSelected: Boolean,
                                            cellHasFocus: Boolean): Component =  {

    val isStarred = list.getModel.asInstanceOf[HistoryListModel].isStarred(index)
    val text = value.asInstanceOf[String].replaceAll("\n", " | ")
    val c = super.getListCellRendererComponent(list, text,
                                               index, isSelected, cellHasFocus)
    val label = c.asInstanceOf[JLabel]
    label.setBorder(border)
    if (isStarred) label.setIcon(star)

    return label;
  }
}




