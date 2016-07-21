import cats.std.future._
import cats.std._
import cats.data._
import cats.syntax.xor._
import cats.Monad

object Main {

  import scala.concurrent.{ExecutionContext, Future}
  final case class Error()
  final case class User()

  def search(
    first    : XorT[Future, Error, User Xor Option[Long]],
    fallback : XorT[Future, Error, User Xor Long]
  )(implicit ev: Monad[Future]): XorT[Future, Error, User Xor Long] = {

    def lift(v: Long): Xor[Error, User Xor Long] = {
      val inner: User Xor Long = v.right
      inner.right
    }

    first.flatMap { 
      case Xor.Right(Some(v)) => XorT.fromXor[Future]{ lift(v) }
      case _                  => fallback
    }

  }

}
