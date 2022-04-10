package com.catastrophic.app

import android.os.Parcel
import org.mockito.Answers
import org.mockito.ArgumentMatchers.*
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.invocation.InvocationOnMock

class MockParcel {

    companion object {
        @JvmStatic
        fun obtain(): Parcel {
            return MockParcel().mockedParcel
        }
    }

    private var position = 0
    private var store = mutableListOf<Any>()

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    lateinit var mockedParcel: Parcel


    init {

        setupWrites()
        setupReads()
        setupOthers()
    }

    // uncomment when needed for the first time
    private fun setupWrites() {
        mockedParcel = Mockito.mock<Parcel>(Parcel::class.java)
        val answer = { i: InvocationOnMock -> with(store) { add(i.arguments[0]); get(lastIndex) } }
        Mockito.`when`(mockedParcel.writeInt(anyInt())).thenAnswer(answer)
        Mockito.`when`(mockedParcel.writeString(anyString())).thenAnswer(answer)
        // whenever(mockedParcel.writeLong(anyLong())).thenAnswer(answer)
        // whenever(mockedParcel.writeFloat(anyFloat())).thenAnswer(answer)
        // whenever(mockedParcel.writeDouble(anyDouble())).thenAnswer(answer)
    }

    // uncomment when needed for the first time
    private fun setupReads() {
        val answer = { _: InvocationOnMock -> store[position++] }
        Mockito.`when`(mockedParcel.readInt()).thenAnswer(answer)
        Mockito.`when`(mockedParcel.readString()).thenAnswer(answer)
        // whenever(mockedParcel.readLong()).thenAnswer(answer)
        // whenever(mockedParcel.readFloat()).thenAnswer(answer)
        // whenever(mockedParcel.readDouble()).thenAnswer(answer)
    }

    private fun setupOthers() {
        val answer = { i: InvocationOnMock -> position = i.arguments[0] as Int; null }
        Mockito.`when`(mockedParcel.setDataPosition(anyInt())).thenAnswer(answer)
    }

}