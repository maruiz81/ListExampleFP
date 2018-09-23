package maruiz.com.listexamplefp.domain.runner

import arrow.effects.IO
import arrow.effects.async
import arrow.effects.fix
import arrow.effects.monadDefer
import arrow.typeclasses.binding
import arrow.typeclasses.bindingCatch
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI

object BackgroundThreadRunner : Runner {
    override operator fun <A> invoke(request: IO<A>): IO<A> =
            IO.monadDefer().bindingCatch {
                IO.async().run {
                    binding {
                        continueOn(CommonPool)
                        request.bind()
                    }.fix()
                }.bind()
            }.fix().continueOn(UI)
}