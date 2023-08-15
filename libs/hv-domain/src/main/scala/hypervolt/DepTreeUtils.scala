package hypervolt

import scala.io.Source

object DepTreeUtils {
  def findNetty(p: String): Unit =
    findAncestories(p, "netty")
      .map(deduplicateProgeny)
      .map(_.pretty)
      .foreach(System.err.println)

  def findAncestories(p: String, needle: String): Option[Node] = {
    filterProgeny(buildTree(p), needle)
  }

  def deduplicateProgeny(node: Node): Node =
    Node(
      value = node.value,
      children = node.children
        .foldLeft((Set.empty[String], List.empty[Node])) {
          case (cum @ (valuesToRemove, _), cur) if valuesToRemove(cur.value) =>
            cum
          case ((valuesToRemove, cum), cur) =>
            // We assume a library cannot depend on itself so no need to clean our children of ourself
            val cleaned =
              deduplicateProgeny(valuesToRemove.foldLeft(cur)(removeChildren))

            (valuesToRemove ++ allProgeny(cleaned) + cleaned.value) ->
              (cleaned +: cum)
        }
        ._2
    )

  def allProgeny(node: Node): Set[String] =
    node.children.flatMap(n => allProgeny(n) + n.value).toSet

  def removeChildren(node: Node, valueToRemove: String): Node =
    Node(
      node.value,
      node.children.flatMap {
        case Node(`valueToRemove`, _) => None
        case child => Some(removeChildren(child, valueToRemove))
      }
    )

  def filterProgeny(node: Node, needle: String): Option[Node] = {
    val childrenFiltered = node.children.flatMap(filterProgeny(_, needle))
    node match {
      case Node(value, _)
          if value.contains(needle) || childrenFiltered.nonEmpty =>
        Some(Node(value, childrenFiltered))
      case _ => None
    }
  }

  def buildTree(p: String): Node = {
    val asList =
      Source
        .fromFile(p)
        .getLines()
        .map(_.stripPrefix("[info] "))
        .dropWhile(!_.startsWith("hvdomain"))
        .toList

    mkNode(asList.head, asList.tail.map(_.stripPrefix("  ")))
  }

  def mkNode(value: String, linesAtLevel: List[String]): Node = Node(
    value = value,
    children =
      childBlocks(linesAtLevel).map(b => mkNode(b.head, b.tail.map(_.drop(2))))
  )

  def childBlocks(l: List[String]): List[List[String]] =
    l
      .foldLeft(List.empty[List[String]]) {
        case (cum, cur) if cur.startsWith("+-")              => List(cur) +: cum
        case (cum, cur) if cur.stripPrefix("|").trim.isEmpty => cum
        case (cum, cur) if cur.startsWith("| ") || cur.startsWith("  ") =>
          (cur +: cum.head) +: cum.tail
        case (cum, cur) if cur.startsWith("[success]") => cum
        case (_, cur) =>
          throw new IllegalCallerException(s"Cur not match: $cur")
      }
      .map(_.reverse)
      .reverse
}

case class Node(value: String, children: List[Node]) {
  def pretty: String =
    value + "\n" +
      children
        .map(_.pretty.split("\n").map("  " + _).mkString("\n"))
        .mkString("\n")
}
