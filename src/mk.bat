call scalac type.scala id.scala syntax.scala
call scalac typing.scala

call jflex lexer.l
copy /Y YyLex.java mincaml\.
yacc -J -Jextends=mincaml.Syntax -Jpackage=mincaml parser.y
copy /Y Parser.java mincaml\.
copy /Y ParserVal.java mincaml\.

javac -classpath "C:\Program Files\Scala\lib\scala-library.jar;." mincaml/Parser.java mincaml/ParserVal.java mincaml/YyLex.java
call scala mincaml.Parser a.txt
call scalac kNormal.scala 2> t.txt

javac -classpath "C:\Program Files\Scala\lib\scala-library.jar;." test.java
