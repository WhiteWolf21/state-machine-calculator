package com.t_engine.statemachinecalculator;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import com.itis.libs.parserng.android.expressParser.MathExpression;

import java.text.DecimalFormat;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mariuszgromada.math.mxparser.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Boolean isDecimal = false;
    Dictionary operators = new Hashtable();
    int lastID = 0;

    HorizontalScrollView scrollResult, scrollPreResult;
    TextView result, preResult;
    String expression = "0";
    Button buttonAC, buttonDEL, buttonPercentage, buttonDivide, buttonMultiply, buttonMinus, buttonAdd, buttonEqual, buttonDecimal;
    Button buttonZero, buttonOne, buttonTwo, buttonThree, buttonFour, buttonFive, buttonSix, buttonSeven, buttonEight, buttonNine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.activity_main);

        result = findViewById(R.id.result);
        scrollResult = findViewById(R.id.scrollResult);
        preResult = findViewById(R.id.preResult);
        scrollPreResult = findViewById(R.id.scrollPreResult);

        //// Operators

        // AC
        buttonAC = findViewById(R.id.buttonAC);
        buttonAC.setOnClickListener(this);

        // Remove
        buttonDEL = findViewById(R.id.buttonDEL);
        buttonDEL.setOnClickListener(this);

        // Percentage
        buttonPercentage = findViewById(R.id.buttonPercentage);
        buttonPercentage.setOnClickListener(this);

        // Divide
        buttonDivide = findViewById(R.id.buttonDivide);
        buttonDivide.setOnClickListener(this);

        // Multiply
        buttonMultiply = findViewById(R.id.buttonMultiply);
        buttonMultiply.setOnClickListener(this);

        // Minus
        buttonMinus = findViewById(R.id.buttonMinus);
        buttonMinus.setOnClickListener(this);

        // Add
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(this);

        // Equal
        buttonEqual = findViewById(R.id.buttonEqual);
        buttonEqual.setOnClickListener(this);

        // Decimal
        buttonDecimal = findViewById(R.id.buttonDecimal);
        buttonDecimal.setOnClickListener(this);

        // Operators List
        operators.put(buttonDivide.getText().toString(), "/");
        operators.put(buttonMultiply.getText().toString(), "*");
        operators.put(buttonMinus.getText().toString(), "-");
        operators.put(buttonAdd.getText().toString(), "+");

        //// Numbers

        // 0
        buttonZero = findViewById(R.id.buttonZero);
        buttonZero.setOnClickListener(this);

        // 1
        buttonOne = findViewById(R.id.buttonOne);
        buttonOne.setOnClickListener(this);

        // 2
        buttonTwo = findViewById(R.id.buttonTwo);
        buttonTwo.setOnClickListener(this);

        // 3
        buttonThree = findViewById(R.id.buttonThree);
        buttonThree.setOnClickListener(this);

        // 4
        buttonFour = findViewById(R.id.buttonFour);
        buttonFour.setOnClickListener(this);

        // 5
        buttonFive = findViewById(R.id.buttonFive);
        buttonFive.setOnClickListener(this);

        // 6
        buttonSix = findViewById(R.id.buttonSix);
        buttonSix.setOnClickListener(this);

        // 7
        buttonSeven = findViewById(R.id.buttonSeven);
        buttonSeven.setOnClickListener(this);

        // 8
        buttonEight = findViewById(R.id.buttonEight);
        buttonEight.setOnClickListener(this);

        // 9
        buttonNine = findViewById(R.id.buttonNine);
        buttonNine.setOnClickListener(this);
    }

    public String realTimeResult(String expr){

        Log.d(" ORI --- ", expr);

        String temp;

        Pattern pattern = Pattern.compile("%[0-9]");
        Matcher matcher = pattern.matcher(expr);

        if (matcher.find()){
            temp = expr.replaceAll("%", "%*");
        }
        else{
            temp = expr;
        }

        if (temp.equals("") == false){
            String ope = Character.toString(temp.charAt(0));
            if (ope.equals("–")){
                temp = temp.replaceFirst("^.","-");
            }
        }

        Boolean check = true;

        while (check){
            if (temp.length() == 0){
                break;
            }
            String num = Character.toString(temp.charAt(temp.length()-1));
            Log.d(" TEMP --- ", temp);
            Log.d(" EXPR --- ", num);
            try
            {
                Double.parseDouble(num);
                break;
            }
            catch(NumberFormatException e)
            {
                Log.d("WRONG TEMP --- ", temp);
                if (num.equals("%")){
                    check = false;
                }
                else{
                    temp = temp.replaceFirst(".$","");
                }
            }
        }

        Expression e = new Expression(temp);
        Double d = e.calculate();
        String calculatedResult;
        if(d % 1 == 0) {
            if (Math.round(d) >= Math.pow(10, 10)){
                calculatedResult = String.valueOf( d );
            }
            else{
                calculatedResult = String.valueOf( Math.round(d) );
            }
        }
        else{
            calculatedResult = String.valueOf( d );
        }
        return calculatedResult;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {

        String display = ((Button)v).getText().toString();

        if(v.getId() == R.id.buttonAC){
            result.setText("0");
            expression = "0";
            preResult.setText("");
        }
        else if(v.getId() == R.id.buttonDEL){
            result.setText(result.getText().toString().replaceFirst(".$",""));
            expression = expression.replaceFirst(".$","");
            Log.d("DEL EXPR --- ", expression);
            if (result.getText().toString().equals("")){
                result.setText("0");
                expression = "0";
            }
        }
        else if(v.getId() == R.id.buttonEqual){
            if (preResult.getText().toString() != ""){
                String equalResult = preResult.getText().toString();
                if (equalResult.equals("NaN") || equalResult.equals("Infinity")){
                    result.setText("0");
                    expression = "0";
                }
                else{
                    result.setText(preResult.getText().toString());
                    expression = preResult.getText().toString();
                }

                preResult.setText("");
            }
        }
        else if (result.getText().toString().equals("0")) {
            result.setText(display);
            expression = display;
        }
        else {

            String exp;
            String res = result.getText().toString();

            if (!Character.toString(res.charAt(res.length()-1)).equals(display) || operators.get(Character.toString(res.charAt(res.length()-1))) == null){

                if (operators.get(Character.toString(res.charAt(res.length()-1))) != null && operators.get(display) != null){
                    result.setText(result.getText().toString().replaceFirst(".$",""));
                    expression = expression.replaceFirst(".$","");
                }

                if (v.getId() == R.id.buttonDivide) {
                    exp = "/";
                }
                else if (v.getId() == R.id.buttonMultiply) {
                    exp = "*";
                }
                else if (v.getId() == R.id.buttonMinus) {
                    exp = "-";
                }
                else {
                    exp = display;
                    Log.d(" WHY V --- ", String.valueOf(v.getId()));
                    Log.d(" WHY M --- ", String.valueOf(R.id.buttonMinus));
                }

                String temp = result.getText().toString() + display;

                Pattern pattern = Pattern.compile("\\.[0-9]*\\.");
                Matcher matcher = pattern.matcher(temp);

                if (matcher.find() == false){
                    result.setText(result.getText().toString() + display);
                    expression = expression + exp;
                }
            }
        }

        if (expression == "0"){
            preResult.setText("");
        }
        else{
            preResult.setText(realTimeResult(expression));
        }

        Log.d("STRANGE -- ", "SSS");

        Log.d("CAL -- ", realTimeResult(expression));

        scrollResult.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
        scrollPreResult.fullScroll(HorizontalScrollView.FOCUS_RIGHT);

    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

}