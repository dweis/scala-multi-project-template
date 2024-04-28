package com.example.server

import cats.effect.IO
import org.http4s._
import org.http4s.implicits._
import munit.CatsEffectSuite

class ExmapleRoutesSpec extends CatsEffectSuite {
  test("/html returns status code 200") {
    assertIO(retHtml.map(_.status) ,Status.Ok)
  }

  test("/html returns html message") {
    assertIO(retHtml.flatMap(_.as[String]), """<!DOCTYPE html><html lang="en"><head><title>Hello, world!</title></head><body><h1>Hello, world!</h1></body></html>""")
  }

  private[this] val retHtml: IO[Response[IO]] = {
    val getHW = Request[IO](Method.GET, uri"/html")
    val exampleImpl = ExampleAlg.impl[IO]
    ExampleRoutes.exampleRoutes(exampleImpl).orNotFound(getHW)
  }

  test("/json returns status code 200") {
    assertIO(retJson.map(_.status) ,Status.Ok)
  }

  test("/json returns json message") {
    assertIO(retJson.flatMap(_.as[String]), """{"id":1,"userId":1,"title":"Simple JSON Example","completed":false}""")
  }

  private[this] val retJson: IO[Response[IO]] = {
    val getHW = Request[IO](Method.GET, uri"/json")
    val exampleImpl = ExampleAlg.impl[IO]
    ExampleRoutes.exampleRoutes(exampleImpl).orNotFound(getHW)
  }
}