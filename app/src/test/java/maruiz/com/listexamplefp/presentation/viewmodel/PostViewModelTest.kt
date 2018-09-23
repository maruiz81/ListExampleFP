package maruiz.com.listexamplefp.presentation.viewmodel

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import maruiz.com.listexamplefp.data.model.PostModel
import maruiz.com.listexamplefp.data.service.PostService
import maruiz.com.listexamplefp.di.context.PostContext
import maruiz.com.listexamplefp.domain.runner.SingleThreadRunner
import maruiz.com.listexamplefp.presentation.view.activity.PostActivity
import maruiz.com.listexamplefp.presentation.view.viewodel.PostViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Response
import java.net.UnknownHostException

@RunWith(MockitoJUnitRunner.Silent::class)
class PostViewModelTest {
    @Mock
    private lateinit var mockView: PostActivity

    @Mock
    private lateinit var mockApiClient: PostService

    private lateinit var viewModel: PostViewModel

    private lateinit var mockContext: PostContext

    @Before
    fun setup() {
        mockContext = PostContext(mockView, SingleThreadRunner, mockApiClient)
        viewModel = PostViewModel()
    }

    @Test
    fun `Test sever returning data`() {
        val resultList = somePosts(10)
        givenPosts(resultList)

        viewModel.getPosts().run(mockContext)

        themPostRenderers(resultList)
    }

    private fun givenPosts(resultList: List<PostModel>) {
        val mockCall = Mockito.mock(Call::class.java) as Call<List<PostModel>>
        val mockResponse = Mockito.mock(Response::class.java) as Response<List<PostModel>>
        whenever(mockApiClient.getPosts()).thenReturn(mockCall)
        whenever(mockCall.execute()).thenReturn(mockResponse)
        whenever(mockResponse.body()).thenReturn(resultList)
    }

    private fun somePosts(count: Int): List<PostModel> = (1..count).map { mockPost(it) }

    private fun mockPost(index: Int): PostModel = mock {
        on { it.title } doReturn "Title $index"
        on { it.body } doReturn "Body $index"
    }

    private fun themPostRenderers(resultList: List<PostModel>) {
        viewModel.postList.observeForever {
            assertEquals(resultList.size, it!!.size)
            it.zip(resultList).forEach {
                assertEquals(it.first.title, it.second.title)
                assertEquals(it.first.body, it.second.body)
            }
        }
    }

    @Test
    fun `Test server return error`() {
        val errorText = "Test error"
        val error = UnknownHostException(errorText)
        givenError(error)

        viewModel.getPosts().run(mockContext)
        themError(errorText)
    }

    private fun givenError(exception: UnknownHostException) {
        val mockCall = Mockito.mock(Call::class.java) as Call<List<PostModel>>
        whenever(mockApiClient.getPosts()).thenReturn(mockCall)
        whenever(mockCall.execute()).thenThrow(exception)
    }

    private fun themError(errorText: String) {
        viewModel.error.observeForever {
            assertEquals(it, errorText)
        }
    }

}
