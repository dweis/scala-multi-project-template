package com.example.server

import cats.effect._
import cats.effect.IO.asyncForIO
import cats.effect.std.Env
import com.comcast.ip4s.{Hostname,Port}

sealed trait ServerError extends RuntimeException
case object InvalidAddrError extends ServerError
case object InvalidPortError extends ServerError

object Main extends IOApp.Simple {
  val defaultAddr = "0.0.0.0"
  val defaultPort = "8080"

  val run = for {
    serverAddr <- Env[IO]
      .get("ADDR")
      .map(_.getOrElse(defaultAddr))
      .flatMap(Hostname.fromString(_) match {
        case Some(addr) => IO.pure(addr)
        case None       => IO.raiseError(InvalidAddrError)
      })
    serverPort <- Env[IO]
      .get("PORT")
      .map(_.getOrElse(defaultPort))
      .flatMap(Port.fromString(_) match {
        case Some(port) => IO.pure(port)
        case None       => IO.raiseError(InvalidPortError)
      })
    _ <- Server.run(serverAddr, serverPort)
  } yield ()
}