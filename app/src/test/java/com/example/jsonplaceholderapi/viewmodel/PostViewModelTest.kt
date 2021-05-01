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
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.HttpException

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PostViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var mockRepo: PostRepository

    @Mock
    private lateinit var responseObserver: Observer<ResultState<ArrayList<PostsItem>>>

    private lateinit var objectUnderTest: PostViewModel

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
    fun `when calling for results then return loading`() {
        testCoroutineRule.runBlockingTest {
            objectUnderTest.posts.observeForever(responseObserver)

            objectUnderTest.fetchPosts()

            Mockito.verify(responseObserver).onChanged(ResultState.InProgress)
        }
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

    @Test
    fun `when fetching results fails then return an error`() {
        val exception = Mockito.mock(HttpException::class.java)
        testCoroutineRule.runBlockingTest {
            objectUnderTest.posts.observeForever(responseObserver)

            Mockito.`when`(mockRepo.getPosts()).thenAnswer {
                ResultState.Error(exception)
            }

            objectUnderTest.fetchPosts()

            assertNotNull(objectUnderTest.posts.value)
            assertEquals(ResultState.Error(exception), objectUnderTest.posts.value)
        }
    }
}