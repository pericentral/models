package org.nlogo.models

import org.scalatest.FunSuite

trait TestModels extends FunSuite {

  def testLibraryModels(testName: String)(testFun: Model => Iterable[Any]): Unit =
    testModels(Model.libraryModels, testName, testFun)

  def testAllModels(testName: String)(testFun: Model => Iterable[Any]): Unit =
    testModels(Model.allModels, testName, testFun)

  def testModels(models: Iterable[Model], testName: String, testFun: Model => Iterable[Any]): Unit =
    test(testName) {
      val allFailures = for {
        model <- models.par
        failures = testFun(model)
        if failures.nonEmpty
      } yield model.quotedPath + failures.mkString("\n  ", "\n  ", "\n")
      if (allFailures.nonEmpty) fail(allFailures.mkString("\n"))
    }

  def testLines(section: Model => String, p: String => Boolean,
    msg: String => String = _ => "")(model: Model): Iterable[String] = {
    (for {
      (line, lineNumber) <- section(model).lines.zipWithIndex
      if p(line)
    } yield "  " + msg(line) + "line %4d |".format(lineNumber) + line).toIterable
  }

}
