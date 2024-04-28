package com.example.server

import cats.effect.Async
import com.comcast.ip4s._
import fs2.io.net.Network
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import org.http4s.server.middleware.Logger

object Server {
  def run[F[_]: Async: Network](addr: Host, port: Port): F[Nothing] = {
    val exampleAlg = ExampleAlg.impl[F]
    val httpApp = (ExampleRoutes.exampleRoutes[F](exampleAlg)).orNotFound
    val finalHttpApp = Logger.httpApp(false, false)(httpApp)
    for {
      _ <- EmberServerBuilder
        .default[F]
        .withHost(addr)
        .withPort(port)
        .withHttpApp(finalHttpApp)
        .build
    } yield ()
  }.useForever
}
