package maruiz.com.listexamplefp.data.util

import arrow.core.Try
import arrow.effects.IO

fun <A, B> tryRunAndFoldResultORError(f: () -> A, onError: (Throwable) -> B, onSuccess: (A) -> B): IO<B> =
        IO { Try { f() }.fold(onError, onSuccess) }