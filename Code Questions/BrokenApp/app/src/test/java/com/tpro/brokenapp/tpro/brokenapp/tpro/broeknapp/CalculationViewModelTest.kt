package com.tpro.brokenapp.tpro.brokenapp.tpro.broeknapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.math.BigDecimal

@ExperimentalCoroutinesApi
class CalculationViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: CalculationViewModel

    @Mock
    private lateinit var resultObserver: Observer<String>

    @Mock
    private lateinit var errorObserver: Observer<String>

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = CalculationViewModel()
        viewModel.result.observeForever(resultObserver)
        viewModel.error.observeForever(errorObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        viewModel.result.removeObserver(resultObserver)
        viewModel.error.removeObserver(errorObserver)
    }

    @Test
    fun `addLargeNumbers with valid inputs post result`() = runTest {
        val num1 = "12345678901234567890"
        val num2 = "98765432109876543210"
        val expectedResult = BigDecimal(num1).add(BigDecimal(num2)).toString()

        viewModel.addLargeNumbers(num1, num2)

        val actualResult = viewModel.result
        advanceUntilIdle()
        assertEquals(expectedResult, actualResult.value)
    }


    @Test
    fun `addLargeNumbers with invalid inputs should post error`() = runTest {
        val num1 = "invalid"
        val num2 = "1234"

        viewModel.addLargeNumbers(num1, num2)

        advanceUntilIdle()

        verify(errorObserver).onChanged("Number is in invalid format")
        verifyNoMoreInteractions(errorObserver)
        verifyNoInteractions(resultObserver)
    }

    @Test
    fun `addLargeNumbers with empty inputs should post error`() = runTest {
        val num1 = ""
        val num2 = ""

        viewModel.addLargeNumbers(num1, num2)

        advanceUntilIdle()

        verify(errorObserver).onChanged("Number is in invalid format")
        verifyNoMoreInteractions(errorObserver)
        verifyNoInteractions(resultObserver)
    }
}
