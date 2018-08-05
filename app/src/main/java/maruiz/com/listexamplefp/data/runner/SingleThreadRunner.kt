package maruiz.com.listexamplefp.data.runner

import arrow.core.Try
import arrow.core.right
import arrow.effects.IO
import arrow.effects.async

class SingleThreadRunner : BackgroundRunner() {
    override fun <A, B> runInAsyncContext(f: () -> A,
                                          onError: (Throwable) -> B,
                                          onSuccess: (A) -> B): IO<B> =
            IO.async().async { callback ->
                callback(Try { f() }.fold(onError, onSuccess).right())
            }
}