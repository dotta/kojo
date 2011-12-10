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

package net.kogics.kojo.inspect

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.BeanNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

class ObjectInspectorNode(name: String, obj: AnyRef) extends BeanNode(new ObjectWrapper(obj)) {
  setChildren(InspectorChildren(obj))
  setDisplayName(name)
  setIconBaseWithExtension("images/field.gif")

  override def getPreferredAction = null
}

class FieldInspectorNode(field: Field, obj: AnyRef) extends BeanNode(new FieldWrapper(field, obj)) {
  setChildren(InspectorChildren(field, obj))
  setDisplayName(field.getName)
  setIconBaseWithExtension("images/field.gif")

  override def getPreferredAction = null
}

// Provides children nodes for supplied object
object InspectorChildren {

  val primitives: Set[Class[_]] = Set(
    java.lang.Boolean.TYPE, 
    java.lang.Character.TYPE, 
    java.lang.Byte.TYPE, 
    java.lang.Short.TYPE, 
    java.lang.Integer.TYPE,
    java.lang.Long.TYPE,
    java.lang.Float.TYPE,
    java.lang.Double.TYPE
  )
  
  def apply(obj: AnyRef) = obj match {
    case arr: Array[AnyRef] =>
      new ArrayChildren(arr)
    case _ =>
      new ObjectWithFieldsChildren(obj)
  }

  def apply(field: Field, obj: AnyRef) = {
    if (primitives.contains(field.getType)) {
      Children.LEAF
    }
    else field.get(obj) match {
      case arr: Array[AnyRef] =>
        new ArrayChildren(arr)
      case fieldVal =>
        new ObjectWithFieldsChildren(fieldVal)
    }
  }
}

class ArrayChildren(arr: Array[AnyRef]) extends Children.Keys[Int] {
  val arrayNodes = new HashMap[Int, Node]()

  override def addNotify() {
    val keys = new ArrayList[Int]();
    for (idx <- 0 until arr.size) {
      keys.add(idx)
      arrayNodes.put(idx, new ObjectInspectorNode(idx.toString, arr(idx)))
    }

    if (keys.size > 0) setKeys(keys)
    else setKeys(Collections.emptySet)
  }

  def createNodes(key: Int): Array[Node] =  {
    Array(arrayNodes.get(key))
  }
}

class ObjectWithFieldsChildren(obj: AnyRef) extends Children.Keys[String] {
  val fieldNodes = new HashMap[String, Node]()

  val Statics = "Static Fields"
  val staticKeys = new ArrayList[String]()
  val staticNodes = new HashMap[String, Node]()

  val Inherited = "Inherited Fields"
  val inheritedKeys = new ArrayList[String]()
  val inheritedNodes = new HashMap[String, Node]()

  val IStatics = "Inherited Static Fields"
  val istaticKeys = new ArrayList[String]()
  val istaticNodes = new HashMap[String, Node]()


  override def addNotify() {
    val keys = new ArrayList[String]();
    obj match {
      case null =>
      case r: AnyRef =>
        val fields = r.getClass().getDeclaredFields()
        fields.foreach { field =>
          field.setAccessible(true);
          if (Modifier.isStatic(field.getModifiers)) {
            staticKeys.add(field.getName)
            staticNodes.put(field.getName, new FieldInspectorNode(field, obj))
          }
          else {
            keys.add(field.getName)
            fieldNodes.put(field.getName, new FieldInspectorNode(field, obj))
          }
        }
        var superClass = r.getClass().getSuperclass
        while (superClass != null) {
          val fields = superClass.getDeclaredFields()
          fields.foreach { field =>
            field.setAccessible(true);
            if (Modifier.isStatic(field.getModifiers)) {
              istaticKeys.add(field.getName)
              istaticNodes.put(field.getName, new FieldInspectorNode(field, obj))
            }
            else {
              inheritedKeys.add(field.getName)
              inheritedNodes.put(field.getName, new FieldInspectorNode(field, obj))
            }
          }
          superClass = superClass.getSuperclass
        }

    }

    Collections.sort(keys)

    if (inheritedKeys.size > 0) {
      Collections.sort(inheritedKeys)
      keys.add(Inherited)
    }

    if (staticKeys.size > 0) {
      Collections.sort(staticKeys)
      keys.add(Statics)
    }

    if (istaticKeys.size > 0) {
      Collections.sort(istaticKeys)
      keys.add(IStatics)
    }

    if (keys.size > 0) setKeys(keys)
    else setKeys(Collections.emptySet)
  }
  
  def createNodes(key: String): Array[Node] = key match {

    case Inherited =>
      Array(new AbstractNode(new ListChildren(inheritedKeys, inheritedNodes)) {
          setDisplayName(Inherited)
          setIconBaseWithExtension("images/container.gif")
        })

    case Statics =>
      Array(new AbstractNode(new ListChildren(staticKeys, staticNodes)) {
          setDisplayName(Statics)
          setIconBaseWithExtension("images/container.gif")
        })

    case IStatics =>
      Array(new AbstractNode(new ListChildren(istaticKeys, istaticNodes)) {
          setDisplayName(IStatics)
          setIconBaseWithExtension("images/container.gif")
        })

    case fieldName =>
      Array(fieldNodes.get(fieldName))
  }
}

class ListChildren[T](list: List[T], map: Map[T, Node]) extends Children.Keys[T] {
  override def addNotify() {
    setKeys(list)
  }

  override def createNodes(key: T): Array[Node] = {
    Array(map.get(key))
  }
}

class ObjectWrapper(obj: AnyRef) {
  def getId = System.identityHashCode(obj).toString

  def getValue = VarFormatter.getValue(obj)

  def getType: String = obj match {
    case r: AnyRef => r.getClass.getName
    case null => "Null"
    case _ => "primitive"
  }
}

class FieldWrapper(field: Field, obj: AnyRef) {
  val fVal = field.get(obj)

  def getId = System.identityHashCode(fVal).toString

  def getValue = VarFormatter.getValue(fVal)

  def getType: String = field.getType.getName
}

object VarFormatter {
  def getValue(obj: AnyRef): String = obj match {
    case null => "null"
    case arr: Array[_] => "[" + arr.mkString(",") + "]"
    case _ => obj.toString
  }
}