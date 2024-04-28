package com.example.server

import cats.Applicative
import cats.implicits._
import com.example.test.Todo
import scalatags.Text.implicits._
import _root_.scalatags.Text.{all => ST}
import _root_.scalatags.Text.tags2.title

trait ExampleAlg[F[_]] {
  def html: F[ST.doctype]
  def json: F[Todo]
}

object ExampleAlg {
  def impl[F[_]: Applicative]: ExampleAlg[F] = new ExampleAlg[F] {
    def html: F[ST.doctype] = ST.doctype("html")(
          ST.html(
            ST.lang := "en",
            ST.head(
              title("Hello, world!")
            ),
            ST.body(
              ST.h1("Hello, world!")
            )
          )
        ).pure[F]
    def json: F[Todo] = Todo(1, 1, "Simple JSON Example", completed = false).pure[F]
  }
}
