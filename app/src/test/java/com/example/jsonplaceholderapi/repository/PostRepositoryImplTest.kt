package com.example.jsonplaceholderapi.repository

import com.example.jsonplaceholderapi.model.PostsItem
import com.example.jsonplaceholderapi.network.JsonPlaceHolderApi
import com.example.jsonplaceholderapi.util.ResponseFileReader
import com.example.jsonplaceholderapi.util.ResultState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@RunWith(JUnit4::class)
class PostRepositoryImplTest {

    private val mockWebServer = MockWebServer()

    private lateinit var mockApi: JsonPlaceHolderApi

    private lateinit var objectUnderTest: PostRepository

    @Before
    fun setUp() {
        mockWebServer.start(8080)

        mockApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
            .create(JsonPlaceHolderApi::class.java)

        objectUnderTest = PostRepositoryImpl(mockApi)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `given list of posts check for equality`() {
        val reader = ResponseFileReader("get_posts.json")
        val gson = Gson()
        val postsListType = object : TypeToken<ArrayList<PostsItem>?>() {}.type
        val expected: ArrayList<PostsItem> = gson.fromJson(reader.content, postsListType)

        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
                    .setResponseCode(200)
                    .setBody(ResponseFileReader("get_posts.json").content)
            }
        }

        runBlocking {
            val posts: ResultState<ArrayList<PostsItem>> = objectUnderTest.getPosts()
            val actualResult = posts.extractData

            assertEquals(expected, actualResult)
        }
    }

    @Test
    fun `given posts item by id check for equality`() {
        val reader = ResponseFileReader("get_post_by_id.json")
        val gson = Gson()
        val expected: PostsItem = gson.fromJson(reader.content, PostsItem::class.java)

        mockWebServer.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return MockResponse()
                    .setResponseCode(200)
                    .setBody(ResponseFileReader("get_post_by_id.json").content)
            }
        }

        runBlocking {
            val actual: PostsItem = objectUnderTest.getPostById(1)

            assertEquals(expected, actual)
        }

    }

    @Test
    fun `given posts item by id check for equality 2`() {
        val reader = ResponseFileReader("get_post_by_id.json")
        val gson = Gson()
        val expected: PostsItem = gson.fromJson(reader.content, PostsItem::class.java)

        mockWebServer.apply {
            enqueue(MockResponse().setBody(ResponseFileReader("get_post_by_id.json").content))
        }

        runBlocking {
            val actual: PostsItem = objectUnderTest.getPostById(1)

            assertEquals(expected, actual)
        }
    }

    @Test
    fun `given posts item by id check for equal property`() {
        mockWebServer.apply {
            enqueue(MockResponse().setBody(ResponseFileReader("get_post_by_id.json").content))
        }

        runBlocking {
            val actual: PostsItem = objectUnderTest.getPostById(1)

            val reader = ResponseFileReader("get_post_by_id.json")
            val gson = Gson()
            val expected: PostsItem = gson.fromJson(reader.content, PostsItem::class.java)

            assertEquals(expected.body, actual.body)
        }
    }

    @Test
    fun `when post request send check for successful creation`() {
        val reader = ResponseFileReader("post_req_for_posts_item.json")
        val gson = Gson()
        val expected: PostsItem = gson.fromJson(reader.content, PostsItem::class.java)

        mockWebServer.apply {
            enqueue(MockResponse().setBody(ResponseFileReader("post_req_for_posts_item.json").content))
        }

        runBlocking {
            val item = PostsItem("Haftbefehl", 111, "DWA", 111)
            val actual: PostsItem = objectUnderTest.postPostsItem(item)
            assertEquals(expected, actual)
        }
    }

    @Test
    fun `given posts item by id check for successful removal`() {
        val expected = PostsItem()

        mockWebServer.apply {
            enqueue(MockResponse().setBody(ResponseFileReader("delete_posts_item_by_id.json").content))
        }

        runBlocking {
            val actual: PostsItem = objectUnderTest.deletePostById(3)

            assertEquals(expected, actual)
        }
    }

    @Test
    fun `given posts item by id check for successful update`() {
        val expected = PostsItem("Pa Sports", 4, "MW", 4)

        mockWebServer.apply {
            enqueue(MockResponse().setBody(ResponseFileReader("put_posts_item_by_id.json").content))
        }

        runBlocking {
            val item = PostsItem("Pa Sports", 4, "MW", 4)
            val actual: PostsItem = objectUnderTest.putPostById(4, item)

            assertEquals(expected, actual)
        }
    }
}