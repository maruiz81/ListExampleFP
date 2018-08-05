package maruiz.com.listexamplefp.data.runner

import arrow.core.Try
import arrow.core.right
import arrow.effects.IO
import arrow.effects.async
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async

open class BackgroundRunner {
    open fun <A, B> runInAsyncContext(f: () -> A, onError: (Throwable) -> B,
                                      onSuccess: (A) -> B): IO<B> =
            IO.async().async { callback ->
                // Calling a Coroutine for the background computation
                async(CommonPool) {
                    val result = Try { f() }.fold(onError, onSuccess)
                    callback(result.right())
                }
            }
}