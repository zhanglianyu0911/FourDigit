/* ----------------------------------------------------------------------------+
 |                    Copyright 2017 Motorola Solutions, Inc.                  |
 |                             All Rights Reserved                             |
 |                   Motorola Solutions Confidential Restricted                |
 +----------------------------------------------------------------------------*/
package com.example.mhjg43.fourdigit;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.graphics.PorterDuff;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class FourDigitFormatInput extends RelativeLayout
                                  implements TextWatcher,
                                             View.OnFocusChangeListener,
                                             View.OnKeyListener {
    private TextView mPinFirstDigitEditText;
    private TextView mPinSecondDigitEditText;
    private TextView mPinThirdDigitEditText;
    private TextView mPinForthDigitEditText;
    private TextView mPinHiddenEditText;
    private String   inputString = "";

    public FourDigitFormatInput(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.four_digit_input, this);
        init(context);
        setPINListeners();
    }

    public FourDigitFormatInput(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        setPINListeners();
    }

    public FourDigitFormatInput(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        setPINListeners();
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.four_digit_input, this);
        mPinFirstDigitEditText  = (TextView) this.findViewById(R.id.pin_first_edittext);
        mPinSecondDigitEditText = (TextView) this.findViewById(R.id.pin_second_edittext);
        mPinThirdDigitEditText  = (TextView) this.findViewById(R.id.pin_third_edittext);
        mPinForthDigitEditText  = (TextView) this.findViewById(R.id.pin_forth_edittext);
        mPinHiddenEditText      = (TextView) this.findViewById(R.id.pin_hidden_edittext);
    }

    private void setPINListeners() {
        mPinHiddenEditText.addTextChangedListener(this);

        mPinFirstDigitEditText.setOnFocusChangeListener(this);
        mPinSecondDigitEditText.setOnFocusChangeListener(this);
        mPinThirdDigitEditText.setOnFocusChangeListener(this);
        mPinForthDigitEditText.setOnFocusChangeListener(this);

        mPinFirstDigitEditText.setOnKeyListener(this);
        mPinSecondDigitEditText.setOnKeyListener(this);
        mPinThirdDigitEditText.setOnKeyListener(this);
        mPinForthDigitEditText.setOnKeyListener(this);
        mPinHiddenEditText.setOnKeyListener(this);
    }

    public void setInputType(int type){
        mPinFirstDigitEditText.setInputType(type);
        mPinSecondDigitEditText.setInputType(type);
        mPinThirdDigitEditText.setInputType(type);
        mPinForthDigitEditText.setInputType(type);
    }

    public void addTextChangedListener(TextWatcher watcher) {
        mPinHiddenEditText.addTextChangedListener(watcher);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (s.length() >= 0 && s.length() <= 4 ) {
            mPinFirstDigitEditText.setText(s + "");
            mPinSecondDigitEditText.setText("");
            mPinThirdDigitEditText.setText("");
            mPinForthDigitEditText.setText("");
        } else if (s.length() >= 5 && s.length() <= 8) {
            mPinSecondDigitEditText.setText(s.subSequence(4, s.length()) + "");
            mPinThirdDigitEditText.setText("");
            mPinForthDigitEditText.setText("");
        } else if (s.length() >= 9 && s.length( )<= 12) {
            mPinThirdDigitEditText.setText(s.subSequence(8, s.length()) + "");

            mPinForthDigitEditText.setText("");
        } else if (s.length() >= 13 && s.length() <= 16) {
            //isValueValid(s.subSequence(12,s.length()),mPinForthDigitEditText);

            mPinForthDigitEditText.setText(s.subSequence(12, s.length()) + "");

            if(s.length()==16){
                InputMethodManager mgr = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(mPinHiddenEditText.getWindowToken(), 0);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        inputString = s.toString();
        filtWhiteSpace(mPinHiddenEditText);

    }

    public String getText(){
        return inputString;
    }

//    public void isValueValid(CharSequence s,TextView textView){
//        if(!GlobalUtils.isAlphaNumeric(s.toString())|| s.length()<4){
//            textView.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
//        }else {
//            textView.getBackground().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
//        }
//    }
//
//    public void setError(){
//        isValueValid(mPinFirstDigitEditText.getText().toString(),mPinFirstDigitEditText);
//        isValueValid(mPinSecondDigitEditText.getText().toString(),mPinSecondDigitEditText);
//        isValueValid(mPinThirdDigitEditText.getText().toString(),mPinThirdDigitEditText);
//        isValueValid(mPinForthDigitEditText.getText().toString(),mPinForthDigitEditText);
//    }
//
//    public void cancelError(){
//        if (GlobalUtils.isOtecIdValueValid(mPinHiddenEditText.getText().toString())){
//            mPinHiddenEditText.setError(null);
//        }
//    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        final int id = v.getId();
        switch (id) {
            case R.id.pin_first_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText, getContext());
                }
                break;
            case R.id.pin_second_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText, getContext());
                }
                break;
            case R.id.pin_third_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText, getContext());
                }
                break;
            case R.id.pin_forth_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText, getContext());
                }
                break;
            default:
                break;
        }
    }

    private static void setFocus(TextView editText) {
        if (editText == null) {
            return;
        }
        editText.setCursorVisible(true);
        editText.setFocusable(true);
        editText.requestFocus();
    }

    private void showSoftKeyboard(TextView editText, Context context) {
        if (editText == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, 0);
    }

    private void filtWhiteSpace(final TextView textView){
        InputFilter filter = new InputFilter() {
            boolean canEnterSpace = false;

            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {

                if(textView.getText().toString().equals(""))
                {
                    canEnterSpace = false;
                }

                StringBuilder builder = new StringBuilder();

                for (int i = start; i < end; i++) {
                    char currentChar = source.charAt(i);
                    if (Character.isLetterOrDigit(currentChar) || currentChar == '_') {
                        builder.append(currentChar);
                        canEnterSpace = true;
                    }

                    if(Character.isWhitespace(currentChar) && canEnterSpace) {
                        builder.append(currentChar);
                    }
                    if(dend==16){
                        textView.setError(null);
                    } else {
                        textView.setError("Only Alpha and Number allow");
                    }
                }
                return builder.toString();
            }

        };

        textView.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(16)});
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            final int id = v.getId();
            switch (id) {
                case R.id.pin_hidden_edittext:
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        if (mPinHiddenEditText.getText().length() <= 12) {
                            mPinForthDigitEditText.setText("");
                        } else if (mPinHiddenEditText.getText().length() <= 8) {
                            mPinThirdDigitEditText.setText("");
                        } else if (mPinHiddenEditText.getText().length() <= 4) {
                            mPinSecondDigitEditText.setText("");
                        } else if (mPinHiddenEditText.getText().length() < 1) {
                            mPinFirstDigitEditText.setText("");
                        }
                        if (mPinHiddenEditText.length() > 0) {
                            mPinHiddenEditText.setText(mPinHiddenEditText.getText().subSequence(0, mPinHiddenEditText.length() - 1));
                        }
                        return true;
                    }
                    break;
                default:
                    return false;
            }
        }
        return false;
    }

}
