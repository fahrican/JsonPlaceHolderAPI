package com.example.jsonplaceholderapi.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.jsonplaceholderapi.TestCoroutineRule
import com.example.jsonplaceholderapi.model.PostsItem
import com.example.jsonplaceholderapi.repository.PostRepository
import com.example.jsonplaceholderapi.util.ResultState
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class PostViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    lateinit var mockRepo: PostRepository

    @Mock
    lateinit var responseObserver: Observer<ResultState<ArrayList<PostsItem>>>

    lateinit var objectUnderTest: PostViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        objectUnderTest = PostViewModel(mockRepo)
    }


    @After
    fun tearDown() {
        objectUnderTest.posts.removeObserver(responseObserver)
    }

    @Test
    fun `when fetching results ok then return a list successfully`() {
        val emptyList = arrayListOf<PostsItem>()
        testCoroutineRule.runBlockingTest {
            objectUnderTest.posts.observeForever(responseObserver)

            Mockito.`when`(mockRepo.getPosts()).thenAnswer {
                ResultState.Success(emptyList)
            }

            objectUnderTest.fetchPosts()

            assertNotNull(objectUnderTest.posts.value)
            assertEquals(ResultState.Success(emptyList), objectUnderTest.posts.value)
        }
    }


    /*
       @Test
       fun `when fetch posts exectuted then check for`() {
           val emptyList = ArrayList<PostsItem>()
           testCoroutineRule.runBlockingTest {

               objectUnderTest.posts.observeForever(responseObserver)

               objectUnderTest.fetchPosts()

               val test = objectUnderTest.posts.getOrAwaitValueTest()

               assertNotNull(objectUnderTest.posts.value)
               //assertNotNull(test)
               //assertEquals(Result.Success(emptyList), viewModel.getPhotos().value)
           }
       }

        @Test
          fun givenServerResponse200_whenFetch_shouldReturnSuccess() {
              testCoroutineRule.runBlockingTest {

                  doReturn(ArrayList<PostsItem>())
                      .`when`(mockRepo)
                      .getPosts()

                  val viewModel = PostViewModel(mockRepo)

                  viewModel.posts.observeForever(apiUsersObserver)

                  verify(mockRepo).getPosts()

                  verify(apiUsersObserver).onChanged(ArrayList<PostsItem>())

                  viewModel.posts.removeObserver(apiUsersObserver)
              }
          }


    @Test
    fun `given succcess state, when fetchMovies called, then isLoading return false`() {
        // Given
        val mockedObserver = createObserver()
        objectUnderTest.status.observeForever(mockedObserver)
        val resource = Resource.Success(ArrayList<PostsItem>())

        coEvery { mockRepo.getPosts() } returns resource.data

        // When
        objectUnderTest.fetchPosts()

        // Then
        val viewStateSlots = mutableListOf<StatusViewState>()
        verify { mockedObserver.onChanged(capture(viewStateSlots)) }

        // Then
        val slot = slot<StatusViewState>()
        verify { mockedObserver.onChanged(capture(slot)) }

        assertEquals(false, slot.captured.isLoading())

        verify { objectUnderTest.fetchPosts() }
    }

    private fun createObserver(): Observer<StatusViewState> = spyk(Observer { })

     */


}