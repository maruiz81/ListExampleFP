package maruiz.com.listexamplefp.presentation.presentation

import com.nhaarman.mockito_kotlin.*
import junit.framework.Assert.assertEquals
import maruiz.com.listexamplefp.data.model.PostModel
import maruiz.com.listexamplefp.data.runner.SingleThreadRunner
import maruiz.com.listexamplefp.data.service.PostService
import maruiz.com.listexamplefp.di.context.PostContext
import maruiz.com.listexamplefp.presentation.view.activity.PostView
import maruiz.com.listexamplefp.presentation.view.viewmodel.PostViewModel
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class PostPresentationTest {
    @Mock
    private lateinit var mockView: PostView
    @Mock
    private lateinit var mockApiClient: PostService

    private lateinit var mockContext: PostContext

    @Before
    fun setup() {
        mockContext = PostContext(mockView, SingleThreadRunner(), mockApiClient)
    }

    @Test
    fun `Test sever returning data`() {
        val resultList = somePosts(10)
        givenPosts(resultList)

        PostPresentation.getPosts().run(mockContext)

        themPostRenderers(resultList)
    }

    fun themPostRenderers(resultList: List<PostModel>) {
        val captor = argumentCaptor<List<PostViewModel>>()
        verify(mockView).drawPost(captor.capture())

        captor.firstValue.zip(resultList).forEach {
            assertEquals(it.first.title, it.second.title)
            assertEquals(it.first.body, it.second.body)
        }
    }

    fun givenPosts(resultList: List<PostModel>) {
        val mockCall = Mockito.mock(Call::class.java) as Call<List<PostModel>>
        val mockResponse = Mockito.mock(Response::class.java) as Response<List<PostModel>>
        whenever(mockCall.execute()).thenReturn(mockResponse)
        whenever(mockResponse.body()).thenReturn(resultList)

        whenever(mockApiClient.getPosts()).thenReturn(mockCall)
    }

    private fun somePosts(count: Int): List<PostModel> = (1..count).map { mockPost(it) }

    private fun mockPost(index: Int): PostModel = mock {
        on { it.id } doReturn index
        on { it.userId } doReturn index * 10
        on { it.title } doReturn "Title $index"
        on { it.body } doReturn "Body $index"
    }
}