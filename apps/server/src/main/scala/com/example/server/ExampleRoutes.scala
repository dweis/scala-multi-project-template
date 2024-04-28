package com.example.server

import cats.effect.Sync
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.circe._
import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import io.circe.generic.auto._
import org.http4s.scalatags._

object ExampleRoutes {
  def exampleRoutes[F[_]: Sync](E: ExampleAlg[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "json" =>
        for {
          json <- E.json
          resp <- Ok(json)
        } yield resp

      case GET -> Root / "html" =>
        for {
          html <- E.html
          resp <- Ok(html)
        } yield resp
    }
  }
}
