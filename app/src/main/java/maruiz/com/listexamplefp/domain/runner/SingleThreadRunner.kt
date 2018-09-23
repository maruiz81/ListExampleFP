package maruiz.com.listexamplefp.domain.runner

import arrow.effects.IO
import arrow.effects.async

object SingleThreadRunner : Runner {
    override operator fun <A> invoke(data: IO<A>): IO<A> =
            IO.async().async {
                data
            }
}