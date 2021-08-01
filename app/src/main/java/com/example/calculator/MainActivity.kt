
package com.example.calculator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import java.lang.NumberFormatException
import kotlinx.android.synthetic.main.activity_main.*
private const val STATE_PENDING_OPERATION="PendingOperation"
private const val STATE_OPERAND1="Operand1"
private const val STATE_OPERAND_STORED="Operand_Stored"
class MainActivity : AppCompatActivity() {
    private var operand1: Double? = null
    private var pendingOperation = "="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val listener = View.OnClickListener { v ->
            val b = v as Button
            newNumber.append(b.text)
        }
        button0.setOnClickListener(listener)
        button1.setOnClickListener(listener)
        button2.setOnClickListener(listener)
        button3.setOnClickListener(listener)
        button4.setOnClickListener(listener)
        button5.setOnClickListener(listener)
        button6.setOnClickListener(listener)
        button7.setOnClickListener(listener)
        button8.setOnClickListener(listener)
        button9.setOnClickListener(listener)
        buttonDot.setOnClickListener(listener)

        val opListener = View.OnClickListener { v ->
            val op = (v as Button).text.toString()
            try {
                val value = newNumber.text.toString().toDouble()
                performOperation(value, op)
            } catch (e: NumberFormatException) {

                newNumber.setText("")

            }
            pendingOperation = op
            operation.text = pendingOperation
        }
        buttonEquals.setOnClickListener(opListener)
        buttonDivide.setOnClickListener(opListener)
        buttonMinus.setOnClickListener(opListener)
        buttonMultiply.setOnClickListener(opListener)
        buttonPlus.setOnClickListener(opListener)

        buttonNeg.setOnClickListener { view ->
            val value = newNumber.text.toString()
            if (value.isEmpty())
                newNumber.setText("-")
            else {
                try {
                    var doubleValue = value.toDouble()
                    doubleValue *= -1
                    newNumber.setText(doubleValue.toString())
                } catch (e: NumberFormatException) {
                    //newNumber was '-' or'.'
                    newNumber.setText("")
                }
            }

        }
    }

    private fun performOperation(
        value: Double,
        operation: String
    ) {
        if (operand1 == null) {
            operand1 = value
        } else {
            if (pendingOperation == "=") {
                pendingOperation = operation
            }
        }
        when (pendingOperation) {
            "=" -> operand1 = value
            "/" -> operand1 = if (value == 0.0) {
                Double.NaN
            } else {
                operand1!! / value
            }
            "X" -> operand1 = operand1!! * value
            "+" -> operand1 = operand1!! + value
            "-" -> operand1 = operand1!! - value
        }
        result.setText(operand1.toString())
        newNumber.setText("")

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if(operand1!=null) {
            outState.putDouble(STATE_OPERAND1, operand1!!)
            outState.putBoolean(STATE_OPERAND_STORED,true)
        }
        outState.putString(STATE_PENDING_OPERATION,pendingOperation)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        operand1 = if (savedInstanceState.getBoolean(STATE_OPERAND_STORED,false)) {
            savedInstanceState.getDouble(STATE_OPERAND1)
        } else
            null
        pendingOperation= savedInstanceState.getString(STATE_PENDING_OPERATION).toString()
        operation.text=pendingOperation
    }
}