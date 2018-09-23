package maruiz.com.listexamplefp.domain.runner

import arrow.effects.IO

interface Runner {
    operator fun <A> invoke(request: IO<A>): IO<A>
}