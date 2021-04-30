package com.example.jsonplaceholderapi.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.jsonplaceholderapi.R
import com.example.jsonplaceholderapi.model.PostsItem
import com.example.jsonplaceholderapi.viewmodel.PostViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.fetchPosts()
        //viewModel.fetchPostById("1")
        //val item = PostsItem("Haftbefehl", 111, "DWA", 111)
        //viewModel.createPostsItem(item)
        //viewModel.removePostById("3")
        //val item = PostsItem("Pa Sports", 4, "MW", 4)
        //viewModel.updatePostById(4, item)
    }

}