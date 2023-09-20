package com.zhao.common.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat

/**
 * @author zjf
 * @date 2023/9/15
 * @effect  键盘工具类
 */
object KeyboardUtil {

    /**
     * 弹出软键盘
     */
    fun showKeyboard(context: Context, editText: EditText) {
        val inputMethodManager = ContextCompat.getSystemService(context, InputMethodManager::class.java)
        inputMethodManager?.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    /**
     * 关闭软键盘
     */
    fun hideKeyboard(context: Context, editText: EditText) {
        val inputMethodManager = ContextCompat.getSystemService(context, InputMethodManager::class.java)
        inputMethodManager?.hideSoftInputFromWindow(editText.windowToken, 0)
    }


    /**
     * 切换软键盘的状态
     */
    fun toggleKeyboard(context: Context, editText: EditText) {
        val inputMethodManager = ContextCompat.getSystemService(context, InputMethodManager::class.java)
        if (inputMethodManager?.isActive(editText) == true) {
            hideKeyboard(context, editText)
        } else {
            showKeyboard(context, editText)
        }
    }

    /**
     * 检查软键盘的状态
     */
    fun isCheckKeyboard(context: Context, editText: EditText): Boolean {
        val inputMethodManager = ContextCompat.getSystemService(context, InputMethodManager::class.java)
        return inputMethodManager?.isActive(editText) ?: false
    }
}