package com.example.jsonplaceholderapi.repository

import com.example.jsonplaceholderapi.model.PostsItem
import com.example.jsonplaceholderapi.network.JsonPlaceHolderApi
import com.example.jsonplaceholderapi.network.JsonPlaceHolderWebService
import com.example.jsonplaceholderapi.util.ResponseFileReader
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class PostRepositoryImplTest2 {

    private lateinit var mockApi: JsonPlaceHolderApi

    private lateinit var objectUnderTest: PostRepository

    @Before
    fun setup() {
        mockApi = JsonPlaceHolderWebService.getApiClient()
        objectUnderTest = PostRepositoryImpl(mockApi)
    }

    private fun createServerAndEnqueue(responseCode: Int, path: String): MockWebServer {
        val server = MockWebServer()
        val response = MockResponse()
            .setResponseCode(responseCode)
            .setBody(ResponseFileReader(path).content)
        server.enqueue(response)

        return server
    }

    internal fun MockWebServer.enqueueResponse(fileName: String, code: Int, path: String) {
        val inputStream = javaClass.classLoader?.getResourceAsStream("api-response/$fileName")

        val source = inputStream?.let { inputStream.source().buffer() }
        source?.let {
            enqueue(
                MockResponse()
                    .setResponseCode(code)
                    .setBody(ResponseFileReader(path).content)
            )
        }
    }

    @Test
    fun `given list of posts check for equality`() = runBlocking {
        val actual: ArrayList<PostsItem> = objectUnderTest.getPosts()

        val reader = ResponseFileReader("get_posts.json")
        val gson = Gson()
        val postsListType = object : TypeToken<ArrayList<PostsItem>?>() {}.type
        val expected: ArrayList<PostsItem> = gson.fromJson(reader.content, postsListType)

        assertEquals(expected, actual)
    }


    @Test
    fun `given posts item by id check for equality`() {
        runBlocking {
            val actual: PostsItem = objectUnderTest.getPostById(1)

            val reader = ResponseFileReader("get_post_by_id.json")
            val gson = Gson()
            val expected: PostsItem = gson.fromJson(reader.content, PostsItem::class.java)

            assertEquals(expected, actual)
        }

    }

    @Test
    fun `given posts item by id check for equality 2`() {
        runBlocking {
            val actual: PostsItem = objectUnderTest.getPostById(1)

            val reader = ResponseFileReader("get_post_by_id.json")
            val gson = Gson()
            val expected: PostsItem = gson.fromJson(reader.content, PostsItem::class.java)

            assertEquals(expected, actual)
        }
    }

    @Test
    fun `given posts item by id check for equal property`() {
        runBlocking {
            val actual: PostsItem = objectUnderTest.getPostById(1)

            val reader = ResponseFileReader("get_post_by_id.json")
            val gson = Gson()
            val expected: PostsItem = gson.fromJson(reader.content, PostsItem::class.java)

            assertEquals(expected.body, actual.body)
        }
    }

    @Test
    fun `when post request send check for successful creation`() = runBlocking {
        val item = PostsItem("Haftbefehl", 111, "DWA", 111)
        val actual: PostsItem = objectUnderTest.postPostsItem(item)

        val reader = ResponseFileReader("post_req_for_posts_item.json")
        val gson = Gson()
        val expected: PostsItem = gson.fromJson(reader.content, PostsItem::class.java)

        assertEquals(expected, actual)
    }


    @Test
    fun `given posts item by id check for successful removal`() = runBlocking {
        val reader = ResponseFileReader("delete_posts_item_by_id.json")
        val gson = Gson()
        val expected: PostsItem = gson.fromJson(reader.content, PostsItem::class.java)

        val actual: PostsItem = objectUnderTest.deletePostById(3)

        assertEquals(expected, actual)
    }

    @Test
    fun `given posts item by id check for successful update`() = runBlocking {
        val reader = ResponseFileReader("put_posts_item_by_id.json")
        val gson = Gson()
        val expected: PostsItem = gson.fromJson(reader.content, PostsItem::class.java)

        val item = PostsItem("Pa Sports", 4, "MW", 4)
        val actual: PostsItem = objectUnderTest.putPostById(4, item)

        assertEquals(expected, actual)
    }

}