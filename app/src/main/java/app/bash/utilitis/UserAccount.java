package app.bash.utilitis;

import android.content.Context;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.EditText;

/**
 * Created by SOHEL KHAN on 10/4/2016.
 */

public class UserAccount {
  //for EditText Refrance
  public static EditText EditTextPointer;
  public static String errorMessage;
  private EditText userName, password;
  public Context mCont;

  public UserAccount(Context mCont, EditText un, EditText pw) {
    this.userName = un;
    this.password = pw;
    this.mCont = mCont;
    isLoginInit(userName, password);
  }

  private static void isLoginInit(EditText userName, EditText password) {
    int maxLength = 10;
    InputFilter[] fArray = new InputFilter[1];
    fArray[0] = new InputFilter.LengthFilter(maxLength);
    //this is for userName
    userName.setHint("Enter Email / Contact No");
    userName.setSingleLine(true);
    userName.setMaxLines(1);
    password.setHint("Enter Passwrod");
    password.setSingleLine(true);
    password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
    password.setMaxLines(1);
    password.setFilters(fArray);
  }

  public static boolean isEmailValid(Context context, EditText tv) {
    //add your own logic
    if (TextUtils.isEmpty(tv.getText())) {
      EditTextPointer = tv;
      EditTextPointer.requestFocus();
      S.T(context, "Email id can't be empty.!");

      errorMessage = "Email id can't be empty.!";
      return false;
    } else {
      if (android.util.Patterns.EMAIL_ADDRESS.matcher(tv.getText()).matches()) {
        return true;
      } else {
        EditTextPointer = tv;
        EditTextPointer.requestFocus();
        S.T(context, "Invalid email id!");
        //  EditTextPointer.setError("enter valid email");
        errorMessage = "invalid email id.!";
        return false;
      }
    }
  }

  public static boolean isPasswordValid(Context context, EditText tv) {
    //add your own logic
    if (tv.getText().toString().length() >= 6) {
      return true;
    } else {
      EditTextPointer = tv;
      errorMessage = "greater than 6 char";
      EditTextPointer.requestFocus();
      S.T(context, "Password should be minimum 6 digit");

      //  EditTextPointer.setError("greater than 6 char");
      return false;
    }
  }

  public static boolean isPhoneLengthValid(Context context, EditText tv) {
    //add your own logic
    if (tv.getText().toString().length() < 8) {
            /*if (!tv.getText().toString().contains("+")) {
                return true;
            } else {*/
      EditTextPointer = tv;
      EditTextPointer.requestFocus();
      S.T(context, "Enter 8 or more digit Phone number");

      //  EditTextPointer.setError("Remove + or county code");
//            errorMessage = "Remove + or county code";
      return false;
//            }
    } else {
            /*EditTextPointer = tv;
            EditTextPointer.requestFocus();
//            S.T(context, "Enter 10 digit Phone number");
            // EditTextPointer.setError("enter 10 digit number");
            errorMessage = "greater than 10 char";*/
      return true;
    }
  }

  public static boolean isValidPhoneNumber(EditText tv) {
    if (tv.getText() == null || TextUtils.isEmpty(tv.getText())) {
      return false;
    } else {
      if (android.util.Patterns.PHONE.matcher(tv.getText()).matches()) {
        return true;
      } else {
        EditTextPointer = tv;
        EditTextPointer.requestFocus();
        EditTextPointer.setError("Enter valid number");
        errorMessage = "invalid mobile number.";
        return false;
      }
    }
  }

  public static boolean isEmpty(EditText... arg) {
    for (int i = 0; i < arg.length; i++) {
      if (arg[i].getText().length() <= 0) {
        EditTextPointer = arg[i];
        EditTextPointer.requestFocus();
        EditTextPointer.setError("this can't be empty");
        return false;
      }

    }
    return true;
  }

   /* public boolean loginViaLocal(String vUserName, String vPassword) {
        //Check for empty values
        if (isEmpty(userName, password)) {
            if (isPasswordValid(password)) {
                String uName, pwd;
                uName = userName.getText().toString().trim();
                pwd = password.getText().toString().trim();
                if (uName.equals(vUserName) && pwd.equals(vPassword)) {
                    return true;
                } else {
                    S.E("Invalid information");
                    return false;
                }
            } else {
                if (EditTextPointer != null) {
                    EditTextPointer.setError(errorMessage);
                }
            }
        } else {
            if (EditTextPointer != null) {
                EditTextPointer.setError("this field is empty");
            }
        }
        return false;
    }*/
}