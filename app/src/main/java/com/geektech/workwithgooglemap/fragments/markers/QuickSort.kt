package com.geektech.workwithgooglemap.fragments.markers

import com.geektech.workwithgooglemap.data.models.Users

class QuickSort {

    fun sort(array: ArrayList<Users>, lowId: Int = 1, highId: Int) {

        if (lowId >= highId) return

        val middle = lowId + (highId - lowId) / 2
        val support = array[middle].id

        var i = lowId
        var j = highId
        while (i <= j) {
            while (array[i].id < support) {
                i++
            }

            while (array[j].id > support) {
                j--
            }

            if (i <= j) {
                val temp = array[i]
                array[i] = array[j]
                array[j] = temp
                i++
                j--
            }
        }

        if (lowId < j) sort(array, lowId, j)

        if (highId > i) sort(array, i, highId)
    }

    fun sort(array: ArrayList<Users>): ArrayList<Users> {
        var isSorted = false
        var buf: Users
        while (!isSorted) {
            isSorted = true
            for (i in 0 until array.size - 1) {
                if (array[i].id > array[i + 1].id) {
                    isSorted = false
                    buf = array[i]
                    array[i] = array[i + 1]
                    array[i + 1] = buf
                }
            }
        }
        return array
    }
}