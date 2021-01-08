package app.bash.activity;

import android.app.Dialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.paytabs.paytabs_sdk.http.APIClient;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Locale;
import java.util.Objects;

import app.bash.R;
import app.bash.data.data_helper.MyCartHelper;
import app.bash.data.data_helper.UserDataHelper;
import app.bash.fragment.AboutUsFragment;
import app.bash.fragment.CartFragment;
import app.bash.fragment.ContactUsFragment;
import app.bash.fragment.HomeFragment;
import app.bash.fragment.InviteFriendFragment;
import app.bash.fragment.InvoiceFragment;
import app.bash.fragment.NotificationFragment;
import app.bash.fragment.QuestionFragment;
import app.bash.fragment.ServiceFragment;
import app.bash.utilitis.ApiClient;
import app.bash.utilitis.ApiInterface;
import app.bash.utilitis.Helper;
import app.bash.utilitis.JSONParser;
import app.bash.utilitis.S;
import app.bash.utilitis.SavedData;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.btn)
    ImageView btn;
    @BindView(R.id.fragment_frame)
    FrameLayout fragmentFrame;
    @BindView(R.id.imgHome)
    ImageView imgHome;
    @BindView(R.id.txtNavHome)
    TextView txtNavHome;
    @BindView(R.id.lnrNavHome)
    LinearLayout lnrNavHome;
    @BindView(R.id.imgService)
    ImageView imgService;
    @BindView(R.id.txtService)
    TextView txtService;
    @BindView(R.id.lnrService)
    LinearLayout lnrService;
    @BindView(R.id.imgNotification)
    ImageView imgNotification;
    @BindView(R.id.txtNotification)
    TextView txtNotification;
    @BindView(R.id.lnrNotification)
    LinearLayout lnrNotification;
    @BindView(R.id.imgCart)
    ImageView imgCart;
    @BindView(R.id.txtCart)
    TextView txtCart;
    @BindView(R.id.lnrCart)
    LinearLayout lnrCart;
    @BindView(R.id.linearBottombar)
    LinearLayout linearBottombar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.tvProfile)
    LinearLayout tvProfile;
    @BindView(R.id.tvInvoice)
    LinearLayout tvInvoice;
    @BindView(R.id.tvQuestion)
    LinearLayout tvQuestion;
    @BindView(R.id.tvAboutUs)
    LinearLayout tvAboutUs;
    @BindView(R.id.tvContactUs)
    LinearLayout tvContactUs;
    @BindView(R.id.tvInviteFriend)
    LinearLayout tvInviteFriend;
    @BindView(R.id.tvChangeLamguage)
    LinearLayout tvChangeLamguage;
    @BindView(R.id.layoutLogout)
    RelativeLayout layoutLogout;
    DrawerLayout drawer;
    @BindView(R.id.btnBack)
    ImageView btnBack;
    @BindView(R.id.btn_sing_in)
    Button btnSingIn;
    @BindView(R.id.btnChat)
    ImageView btnChat;
    @BindView(R.id.titleHome)
    TextView titleHome;
    @BindView(R.id.btnSearch)
    ImageView btnSearch;
    @BindView(R.id.titleService)
    LinearLayout titleService;
    @BindView(R.id.titleNotifications)
    TextView titleNotifications;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tvSubtitle)
    TextView tvSubtitle;
    @BindView(R.id.imageCart)
    ImageView imageCart;
    @BindView(R.id.viewProfile)
    View viewProfile;
    @BindView(R.id.viewInvoice)
    View viewInvoice;

    Fragment fragment;
    FragmentManager fm;
    String selected = "";
    private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        if (SavedData.getLanguage().equals("ar")) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) btn.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            btn.setLayoutParams(params);

            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) btnChat.getLayoutParams();
            params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            btnChat.setLayoutParams(params1);
            btnBack.setLayoutParams(params1);
            btnSearch.setLayoutParams(params1);
        } else {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) btn.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            btn.setLayoutParams(params);

            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) btnChat.getLayoutParams();
            params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            btnChat.setLayoutParams(params1);
            btnBack.setLayoutParams(params1);
            btnSearch.setLayoutParams(params1);
        }

        selected = this.getIntent().getStringExtra("selected");
        fm = getSupportFragmentManager();

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ImageView imageUser = navigationView.getHeaderView(0).findViewById(R.id.imageUser);
        TextView tvUserName = navigationView.getHeaderView(0).findViewById(R.id.tvUserName);
        if (UserDataHelper.getInstance().getUserDataModel().size() > 0) {
            tvUserName.setText(UserDataHelper.getInstance().getUserDataModel().get(0).getFullName());
            if (!UserDataHelper.getInstance().getUserDataModel().get(0).getImage().equals("") && UserDataHelper.getInstance().getUserDataModel().get(0).getImage() != null) {
                Picasso.get().load(UserDataHelper.getInstance().getUserDataModel().get(0).getImage())
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .placeholder(R.drawable.ic_user_nav)
                        .error(R.drawable.ic_user_nav)
                        .into(imageUser);
            }
            layoutLogout.setVisibility(View.VISIBLE);
        } else {
            layoutLogout.setVisibility(View.GONE);
        }
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        toggle.setDrawerIndicatorEnabled(false);
//        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_launcher, getTheme());
//        toggle.setHomeAsUpIndicator(drawable);
        int width = getResources().getDisplayMetrics().widthPixels / 2;
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) navigationView.getLayoutParams();
        params.width = width;
        navigationView.setLayoutParams(params);

        ImageView btn = findViewById(R.id.btn);
        btnChat.setVisibility(View.VISIBLE);
        titleHome.setVisibility(View.VISIBLE);
        btnSearch.setVisibility(View.GONE);
        titleService.setVisibility(View.GONE);
        titleNotifications.setVisibility(View.GONE);
        imageCart.setVisibility(View.GONE);

       /* fragment = fm.findFragmentByTag("home");
        if (fragment == null) {
            FragmentTransaction ft = fm.beginTransaction();
            fragment = new HomeFragment();
            ft.replace(R.id.fragment_frame, fragment, "home");
            ft.commit();
        }
        Drawable unwrappedDrawableHome = AppCompatResources.getDrawable(MainActivity.this, R.drawable.ic_home_1_blue);
        Drawable wrappedDrawablePaymentHome = DrawableCompat.wrap(unwrappedDrawableHome);
        DrawableCompat.setTint(wrappedDrawablePaymentHome, getResources().getColor(R.color.colorBlue));
        imgHome.setImageDrawable(wrappedDrawablePaymentHome);
        txtNavHome.setTextColor(getResources().getColor(R.color.colorBlue));*/

        if (selected.equals("cart")) {
            fragment = fm.findFragmentByTag("cart");
            if (fragment == null) {
                FragmentTransaction ft = fm.beginTransaction();
                fragment = new CartFragment();
                ft.replace(R.id.fragment_frame, fragment, "cart");
                ft.commit();
            }
            btn.setVisibility(View.VISIBLE);

            linearBottombar.setVisibility(View.GONE);
            imageCart.setVisibility(View.GONE);
            btnBack.setVisibility(View.VISIBLE);
            titleHome.setVisibility(View.GONE);
            btnSearch.setVisibility(View.GONE);
            titleService.setVisibility(View.GONE);
            btnChat.setVisibility(View.GONE);
            titleNotifications.setVisibility(View.GONE);
            Drawable unwrappedDrawableCart3 = AppCompatResources.getDrawable(MainActivity.this, R.drawable.ic_shopping_cart_blue);
            Drawable wrappedDrawableCart3 = DrawableCompat.wrap(unwrappedDrawableCart3);
            DrawableCompat.setTint(wrappedDrawableCart3, getResources().getColor(R.color.colorBlue));
            imgCart.setImageDrawable(wrappedDrawableCart3);
            txtCart.setTextColor(getResources().getColor(R.color.colorBlue));
        } else {
            fragment = fm.findFragmentByTag("home");
            if (fragment == null) {
                FragmentTransaction ft = fm.beginTransaction();
                fragment = new HomeFragment();
                ft.replace(R.id.fragment_frame, fragment, "home");
                ft.commit();
            }
            btn.setVisibility(View.VISIBLE);
            Drawable unwrappedDrawableHome = AppCompatResources.getDrawable(MainActivity.this, R.drawable.ic_home_1_blue);
            Drawable wrappedDrawablePaymentHome = DrawableCompat.wrap(unwrappedDrawableHome);
            DrawableCompat.setTint(wrappedDrawablePaymentHome, getResources().getColor(R.color.colorBlue));
            imgHome.setImageDrawable(wrappedDrawablePaymentHome);
            txtNavHome.setTextColor(getResources().getColor(R.color.colorBlue));
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* int width = getResources().getDisplayMetrics().widthPixels/2;
                DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) navigationView.getLayoutParams();
                params.width = width;
                navigationView.setLayoutParams(params);*/
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.END);
                }
            }
        });

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle1 = new Bundle();
                bundle1.putString("callingFrom", "live");
                //@Rafat-4/1/2021
               // S.I(MainActivity.this, ChatActivity.class, bundle1);
            }
        });

        /*toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.END);
                }
            }
        });*/
        navigationView.setNavigationItemSelectedListener(this);

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_rate);

                RatingBar ratingBarEmployee = dialog.findViewById(R.id.ratingBarEmployee);
                RatingBar ratingBarService = dialog.findViewById(R.id.ratingBarService);

                EditText edtEmployee = dialog.findViewById(R.id.edtEmployee);
                EditText edtService = dialog.findViewById(R.id.edtService);

                Button btnSend = dialog.findViewById(R.id.btnSend);

                btnSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new JSONParser(MainActivity.this).parseRetrofitRequest(true, ApiClient.getClient().create(ApiInterface.class).addRating(
                                "1",
                                UserDataHelper.getInstance().getUserDataModel().get(0).getUserId(),
                                String.valueOf(ratingBarService.getRating()),
                                String.valueOf(ratingBarEmployee.getRating()),
                                edtService.getText().toString(),
                                edtEmployee.getText().toString()
                        ), new Helper() {
                            @Override
                            public void backResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.getBoolean("status")) {
                                        dialog.dismiss();
                                        S.T(MainActivity.this, "Rating submitted!");
                                    } else {
                                        S.T(MainActivity.this, jsonObject.getString("msg"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
                dialog.show();
            }
        }, 5000);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    @OnClick({R.id.lnrNavHome, R.id.lnrService, R.id.lnrNotification, R.id.lnrCart, R.id.tvProfile,
            R.id.tvInvoice, R.id.tvQuestion, R.id.tvAboutUs, R.id.tvContactUs, R.id.tvInviteFriend,
            R.id.layoutLogout, R.id.btnBack, R.id.btnChat, R.id.btn_sing_in, R.id.tvChangeLamguage})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.lnrNavHome:

                btn.setVisibility(View.VISIBLE);
                btnChat.setVisibility(View.VISIBLE);
                titleHome.setVisibility(View.VISIBLE);
                linearBottombar.setVisibility(View.VISIBLE);
                btnSearch.setVisibility(View.GONE);
                titleService.setVisibility(View.GONE);
                btnBack.setVisibility(View.GONE);
                titleNotifications.setVisibility(View.GONE);
                imageCart.setVisibility(View.GONE);
                fragment = fm.findFragmentByTag("home");
                if (fragment == null) {
                    FragmentTransaction ft = fm.beginTransaction();
                    fragment = new HomeFragment();
                    ft.replace(R.id.fragment_frame, fragment, "home");
                    ft.commit();
                }
                Drawable unwrappedDrawableHome = AppCompatResources.getDrawable(MainActivity.this, R.drawable.ic_home_1_blue);
                Drawable wrappedDrawablePaymentHome = DrawableCompat.wrap(unwrappedDrawableHome);
                DrawableCompat.setTint(wrappedDrawablePaymentHome, getResources().getColor(R.color.colorBlue));
                imgHome.setImageDrawable(wrappedDrawablePaymentHome);
                txtNavHome.setTextColor(getResources().getColor(R.color.colorBlue));

                Drawable unwrappedDrawableService = AppCompatResources.getDrawable(MainActivity.this, R.drawable.ic_document_gray);
                Drawable wrappedDrawableService = DrawableCompat.wrap(unwrappedDrawableService);
                DrawableCompat.setTint(wrappedDrawableService, getResources().getColor(R.color.colorTextSecondary));
                imgService.setImageDrawable(wrappedDrawableService);
                txtService.setTextColor(getResources().getColor(R.color.colorTextSecondary));

                Drawable unwrappedDrawableNotification = AppCompatResources.getDrawable(MainActivity.this, R.drawable.ic_bell_gray);
                Drawable wrappedDrawableNotification = DrawableCompat.wrap(unwrappedDrawableNotification);
                DrawableCompat.setTint(wrappedDrawableNotification, getResources().getColor(R.color.colorTextSecondary));
                imgNotification.setImageDrawable(wrappedDrawableNotification);
                txtNotification.setTextColor(getResources().getColor(R.color.colorTextSecondary));

                Drawable unwrappedDrawableCart = AppCompatResources.getDrawable(MainActivity.this, R.drawable.ic_shopping_cart);
                Drawable wrappedDrawableCart = DrawableCompat.wrap(unwrappedDrawableCart);
                DrawableCompat.setTint(wrappedDrawableCart, getResources().getColor(R.color.colorTextSecondary));
                imgCart.setImageDrawable(wrappedDrawableCart);
                txtCart.setTextColor(getResources().getColor(R.color.colorTextSecondary));

                break;
            case R.id.lnrService:
                if (UserDataHelper.getInstance().getUserDataModel().size() > 0) {
                    btn.setVisibility(View.VISIBLE);
                    linearBottombar.setVisibility(View.VISIBLE);
                    btnSearch.setVisibility(View.VISIBLE);
                    titleService.setVisibility(View.VISIBLE);
                    btnBack.setVisibility(View.GONE);
                    titleHome.setVisibility(View.GONE);
                    btnChat.setVisibility(View.GONE);
                    titleNotifications.setVisibility(View.GONE);
                    imageCart.setVisibility(View.GONE);
                    title.setText(getString(R.string.service));
                    tvSubtitle.setText(getString(R.string.here_you_see_all_your_services));
                    fragment = fm.findFragmentByTag("service");
                    if (fragment == null) {
                        FragmentTransaction ft = fm.beginTransaction();
                        fragment = new ServiceFragment();
                        ft.replace(R.id.fragment_frame, fragment, "service");
                        ft.commit();
                    }
                    Drawable unwrappedDrawableHome1 = AppCompatResources.getDrawable(MainActivity.this, R.drawable.ic_hime_1_gray);
                    Drawable wrappedDrawablePaymentHome1 = DrawableCompat.wrap(unwrappedDrawableHome1);
                    DrawableCompat.setTint(wrappedDrawablePaymentHome1, getResources().getColor(R.color.colorTextSecondary));
                    imgHome.setImageDrawable(wrappedDrawablePaymentHome1);
                    txtNavHome.setTextColor(getResources().getColor(R.color.colorTextSecondary));

                    Drawable unwrappedDrawableService1 = AppCompatResources.getDrawable(MainActivity.this, R.drawable.ic_document_blue);
                    Drawable wrappedDrawableService1 = DrawableCompat.wrap(unwrappedDrawableService1);
                    DrawableCompat.setTint(wrappedDrawableService1, getResources().getColor(R.color.colorBlue));
                    imgService.setImageDrawable(wrappedDrawableService1);
                    txtService.setTextColor(getResources().getColor(R.color.colorBlue));

                    Drawable unwrappedDrawableNotification1 = AppCompatResources.getDrawable(MainActivity.this, R.drawable.ic_bell_gray);
                    Drawable wrappedDrawableNotification1 = DrawableCompat.wrap(unwrappedDrawableNotification1);
                    DrawableCompat.setTint(wrappedDrawableNotification1, getResources().getColor(R.color.colorTextSecondary));
                    imgNotification.setImageDrawable(wrappedDrawableNotification1);
                    txtNotification.setTextColor(getResources().getColor(R.color.colorTextSecondary));

                    Drawable unwrappedDrawableCart1 = AppCompatResources.getDrawable(MainActivity.this, R.drawable.ic_shopping_cart);
                    Drawable wrappedDrawableCart1 = DrawableCompat.wrap(unwrappedDrawableCart1);
                    DrawableCompat.setTint(wrappedDrawableCart1, getResources().getColor(R.color.colorTextSecondary));
                    imgCart.setImageDrawable(wrappedDrawableCart1);
                    txtCart.setTextColor(getResources().getColor(R.color.colorTextSecondary));
                } else {
                    S.I(MainActivity.this, WelcomeActivity.class, null);
                }
                break;
            case R.id.lnrNotification:
                btn.setVisibility(View.VISIBLE);
                linearBottombar.setVisibility(View.VISIBLE);
                titleNotifications.setVisibility(View.VISIBLE);
                titleNotifications.setText(getString(R.string.notification));
                btnBack.setVisibility(View.GONE);
                titleHome.setVisibility(View.GONE);
                btnSearch.setVisibility(View.VISIBLE);
                titleService.setVisibility(View.GONE);
                btnChat.setVisibility(View.GONE);
                imageCart.setVisibility(View.GONE);
                fragment = fm.findFragmentByTag("notification");
                if (fragment == null) {
                    FragmentTransaction ft = fm.beginTransaction();
                    fragment = new NotificationFragment();
                    ft.replace(R.id.fragment_frame, fragment, "notification");
                    ft.commit();
                }
                Drawable unwrappedDrawableHome2 = AppCompatResources.getDrawable(MainActivity.this, R.drawable.ic_hime_1_gray);
                Drawable wrappedDrawablePaymentHome2 = DrawableCompat.wrap(unwrappedDrawableHome2);
                DrawableCompat.setTint(wrappedDrawablePaymentHome2, getResources().getColor(R.color.colorTextSecondary));
                imgHome.setImageDrawable(wrappedDrawablePaymentHome2);
                txtNavHome.setTextColor(getResources().getColor(R.color.colorTextSecondary));

                Drawable unwrappedDrawableService2 = AppCompatResources.getDrawable(MainActivity.this, R.drawable.ic_document_gray);
                Drawable wrappedDrawableService2 = DrawableCompat.wrap(unwrappedDrawableService2);
                DrawableCompat.setTint(wrappedDrawableService2, getResources().getColor(R.color.colorTextSecondary));
                imgService.setImageDrawable(wrappedDrawableService2);
                txtService.setTextColor(getResources().getColor(R.color.colorTextSecondary));

                Drawable unwrappedDrawableNotification2 = AppCompatResources.getDrawable(MainActivity.this, R.drawable.ic_bell_blue);
                Drawable wrappedDrawableNotification2 = DrawableCompat.wrap(unwrappedDrawableNotification2);
                DrawableCompat.setTint(wrappedDrawableNotification2, getResources().getColor(R.color.colorBlue));
                imgNotification.setImageDrawable(wrappedDrawableNotification2);
                txtNotification.setTextColor(getResources().getColor(R.color.colorBlue));

                Drawable unwrappedDrawableCart2 = AppCompatResources.getDrawable(MainActivity.this, R.drawable.ic_shopping_cart);
                Drawable wrappedDrawableCart2 = DrawableCompat.wrap(unwrappedDrawableCart2);
                DrawableCompat.setTint(wrappedDrawableCart2, getResources().getColor(R.color.colorTextSecondary));
                imgCart.setImageDrawable(wrappedDrawableCart2);
                txtCart.setTextColor(getResources().getColor(R.color.colorTextSecondary));

                break;
            case R.id.lnrCart:
                btn.setVisibility(View.VISIBLE);
                linearBottombar.setVisibility(View.VISIBLE);
                imageCart.setVisibility(View.GONE);
                btnBack.setVisibility(View.VISIBLE);
                titleHome.setVisibility(View.GONE);
                btnSearch.setVisibility(View.GONE);
                titleService.setVisibility(View.GONE);
                btnChat.setVisibility(View.GONE);
                titleNotifications.setVisibility(View.GONE);
                fragment = fm.findFragmentByTag("cart");
                if (fragment == null) {
                    FragmentTransaction ft = fm.beginTransaction();
                    fragment = new CartFragment();
                    ft.replace(R.id.fragment_frame, fragment, "cart");
                    ft.commit();
                }
                Drawable unwrappedDrawableHome3 = AppCompatResources.getDrawable(MainActivity.this, R.drawable.ic_hime_1_gray);
                Drawable wrappedDrawablePaymentHome3 = DrawableCompat.wrap(unwrappedDrawableHome3);
                DrawableCompat.setTint(wrappedDrawablePaymentHome3, getResources().getColor(R.color.colorTextSecondary));
                imgHome.setImageDrawable(wrappedDrawablePaymentHome3);
                txtNavHome.setTextColor(getResources().getColor(R.color.colorTextSecondary));

                Drawable unwrappedDrawableService3 = AppCompatResources.getDrawable(MainActivity.this, R.drawable.ic_document_gray);
                Drawable wrappedDrawableService3 = DrawableCompat.wrap(unwrappedDrawableService3);
                DrawableCompat.setTint(wrappedDrawableService3, getResources().getColor(R.color.colorTextSecondary));
                imgService.setImageDrawable(wrappedDrawableService3);
                txtService.setTextColor(getResources().getColor(R.color.colorTextSecondary));

                Drawable unwrappedDrawableNotification3 = AppCompatResources.getDrawable(MainActivity.this, R.drawable.ic_bell_gray);
                Drawable wrappedDrawableNotification3 = DrawableCompat.wrap(unwrappedDrawableNotification3);
                DrawableCompat.setTint(wrappedDrawableNotification3, getResources().getColor(R.color.colorTextSecondary));
                imgNotification.setImageDrawable(wrappedDrawableNotification3);
                txtNotification.setTextColor(getResources().getColor(R.color.colorTextSecondary));

                Drawable unwrappedDrawableCart3 = AppCompatResources.getDrawable(MainActivity.this, R.drawable.ic_shopping_cart_blue);
                Drawable wrappedDrawableCart3 = DrawableCompat.wrap(unwrappedDrawableCart3);
                DrawableCompat.setTint(wrappedDrawableCart3, getResources().getColor(R.color.colorBlue));
                imgCart.setImageDrawable(wrappedDrawableCart3);
                txtCart.setTextColor(getResources().getColor(R.color.colorBlue));

                break;
            case R.id.tvProfile:

                if (UserDataHelper.getInstance().getUserDataModel().size() > 0) {
                    S.I(MainActivity.this, ProfileActivity.class, null);
                } else {
                    S.I(MainActivity.this, WelcomeActivity.class, null);
                }
                break;
            case R.id.tvInvoice:
                if (UserDataHelper.getInstance().getUserDataModel().size() > 0) {
                    drawer.closeDrawer(GravityCompat.END);
                    linearBottombar.setVisibility(View.GONE);
                    btn.setVisibility(View.GONE);
                    btnBack.setVisibility(View.VISIBLE);
                    titleHome.setVisibility(View.GONE);
                    btnSearch.setVisibility(View.GONE);
                    titleService.setVisibility(View.VISIBLE);
                    title.setText(getString(R.string.invoices));
                    tvSubtitle.setText(getString(R.string.see_all_invoices));
                    btnChat.setVisibility(View.GONE);
                    titleNotifications.setVisibility(View.GONE);
                    imageCart.setVisibility(View.GONE);
                    fragment = fm.findFragmentByTag("invoice");
                    if (fragment == null) {
                        FragmentTransaction ft = fm.beginTransaction();
                        fragment = new InvoiceFragment();
                        ft.replace(R.id.fragment_frame, fragment, "invoice");
                        ft.commit();
                    }
                } else {
                    S.I(MainActivity.this, WelcomeActivity.class, null);
                }
                break;
            case R.id.tvQuestion:
                drawer.closeDrawer(GravityCompat.END);
                linearBottombar.setVisibility(View.GONE);
                btn.setVisibility(View.GONE);
                btnBack.setVisibility(View.VISIBLE);
                titleHome.setVisibility(View.GONE);
                btnSearch.setVisibility(View.GONE);
                titleService.setVisibility(View.GONE);
                btnChat.setVisibility(View.GONE);
                imageCart.setVisibility(View.GONE);
                titleNotifications.setVisibility(View.VISIBLE);
                titleNotifications.setText(getString(R.string.common_ques));
                fragment = fm.findFragmentByTag("question");
                if (fragment == null) {
                    FragmentTransaction ft = fm.beginTransaction();
                    fragment = new QuestionFragment();
                    ft.replace(R.id.fragment_frame, fragment, "question");
                    ft.commit();
                }
                break;
            case R.id.tvAboutUs:
                drawer.closeDrawer(GravityCompat.END);
                linearBottombar.setVisibility(View.GONE);
                btn.setVisibility(View.GONE);
                btnBack.setVisibility(View.VISIBLE);
                titleHome.setVisibility(View.GONE);
                btnSearch.setVisibility(View.GONE);
                titleService.setVisibility(View.GONE);
                btnChat.setVisibility(View.GONE);
                imageCart.setVisibility(View.GONE);
                titleNotifications.setVisibility(View.VISIBLE);
                titleNotifications.setText(getString(R.string.aboutus));
                fragment = fm.findFragmentByTag("aboutus");
                if (fragment == null) {
                    FragmentTransaction ft = fm.beginTransaction();
                    fragment = new AboutUsFragment();
                    ft.replace(R.id.fragment_frame, fragment, "aboutus");
                    ft.commit();
                }
                break;
            case R.id.tvContactUs:
                drawer.closeDrawer(GravityCompat.END);
                linearBottombar.setVisibility(View.GONE);
                btn.setVisibility(View.GONE);
                btnBack.setVisibility(View.VISIBLE);
                titleHome.setVisibility(View.GONE);
                btnChat.setVisibility(View.GONE);
                imageCart.setVisibility(View.GONE);
                titleNotifications.setVisibility(View.VISIBLE);
                titleNotifications.setText(getString(R.string.contactus));
                fragment = fm.findFragmentByTag("contactus");
                if (fragment == null) {
                    FragmentTransaction ft = fm.beginTransaction();
                    fragment = new ContactUsFragment();
                    ft.replace(R.id.fragment_frame, fragment, "contactus");
                    ft.commit();
                }
                break;
            case R.id.tvInviteFriend:
                //S.share(MainActivity.this, getString(R.string.app_name), "Click on a link to download the app " + "https://play.google.com/store/apps/details?id=" + getPackageName());
                drawer.closeDrawer(GravityCompat.END);
                linearBottombar.setVisibility(View.GONE);
                btnBack.setVisibility(View.VISIBLE);
                btn.setVisibility(View.GONE);
                titleHome.setVisibility(View.GONE);
                btnSearch.setVisibility(View.GONE);
                titleService.setVisibility(View.GONE);
                btnChat.setVisibility(View.GONE);
                imageCart.setVisibility(View.GONE);
                titleNotifications.setVisibility(View.GONE);
                titleNotifications.setText(getString(R.string.invite_a_friend));
                fragment = fm.findFragmentByTag("invitefriend");
                if (fragment == null) {
                    FragmentTransaction ft = fm.beginTransaction();
                    fragment = new InviteFriendFragment();
                    ft.replace(R.id.fragment_frame, fragment, "invitefriend");
                    ft.commit();
                }
                break;
            case R.id.btnChat:
                break;
            case R.id.btnBack:
                btn.setVisibility(View.VISIBLE);
                btnChat.setVisibility(View.VISIBLE);
                titleHome.setVisibility(View.VISIBLE);
                linearBottombar.setVisibility(View.VISIBLE);
                btnBack.setVisibility(View.GONE);
                btnSearch.setVisibility(View.GONE);
                titleService.setVisibility(View.GONE);
                titleNotifications.setVisibility(View.GONE);
                imageCart.setVisibility(View.GONE);
                fragment = fm.findFragmentByTag("home");
                if (fragment == null) {
                    FragmentTransaction ft = fm.beginTransaction();
                    fragment = new HomeFragment();
                    ft.replace(R.id.fragment_frame, fragment, "home");
                    ft.commit();
                }
                Drawable unwrappedDrawableHome4 = AppCompatResources.getDrawable(MainActivity.this, R.drawable.ic_home_1_blue);
                Drawable wrappedDrawablePaymentHome4 = DrawableCompat.wrap(unwrappedDrawableHome4);
                DrawableCompat.setTint(wrappedDrawablePaymentHome4, getResources().getColor(R.color.colorBlue));
                imgHome.setImageDrawable(wrappedDrawablePaymentHome4);
                txtNavHome.setTextColor(getResources().getColor(R.color.colorBlue));

                Drawable unwrappedDrawableService4 = AppCompatResources.getDrawable(MainActivity.this, R.drawable.ic_document_gray);
                Drawable wrappedDrawableService4 = DrawableCompat.wrap(unwrappedDrawableService4);
                DrawableCompat.setTint(wrappedDrawableService4, getResources().getColor(R.color.colorTextSecondary));
                imgService.setImageDrawable(wrappedDrawableService4);
                txtService.setTextColor(getResources().getColor(R.color.colorTextSecondary));

                Drawable unwrappedDrawableNotification4 = AppCompatResources.getDrawable(MainActivity.this, R.drawable.ic_bell_gray);
                Drawable wrappedDrawableNotification4 = DrawableCompat.wrap(unwrappedDrawableNotification4);
                DrawableCompat.setTint(wrappedDrawableNotification4, getResources().getColor(R.color.colorTextSecondary));
                imgNotification.setImageDrawable(wrappedDrawableNotification4);
                txtNotification.setTextColor(getResources().getColor(R.color.colorTextSecondary));

                Drawable unwrappedDrawableCart4 = AppCompatResources.getDrawable(MainActivity.this, R.drawable.ic_shopping_cart);
                Drawable wrappedDrawableCart4 = DrawableCompat.wrap(unwrappedDrawableCart4);
                DrawableCompat.setTint(wrappedDrawableCart4, getResources().getColor(R.color.colorTextSecondary));
                imgCart.setImageDrawable(wrappedDrawableCart4);
                txtCart.setTextColor(getResources().getColor(R.color.colorTextSecondary));

                break;
            case R.id.layoutLogout:
                openPopupLogout();
                break;
            case R.id.btn_sing_in:
                openPopupLogout();
                break;
            case R.id.tvChangeLamguage:
                alerT();
                break;
        }
    }

    public void alerT() {
        final Dialog dialog = new Dialog(Objects.requireNonNull(this));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_change_language);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
        final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radioGroup);
        RadioButton radioBtnEnglish = (RadioButton) dialog.findViewById(R.id.radioBtnEnglish);
        RadioButton radioBtnSpanish = (RadioButton) dialog.findViewById(R.id.radioBtnSpanish);
        dialog.show();
        dialog.setCancelable(false);

        if (SavedData.getLanguage().equals("")) {
            radioBtnEnglish.setChecked(true);
            radioBtnSpanish.setChecked(false);
        } else {
            if (SavedData.getLanguage().equals("en")) {
                radioBtnEnglish.setChecked(true);
                radioBtnSpanish.setChecked(false);
            } else {
                radioBtnSpanish.setChecked(true);
                radioBtnEnglish.setChecked(false);
            }
        }
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) dialog.findViewById(selectedId);
                String languageToLoad = "";
                if (radioButton.getText().toString().equals(getResources().getString(R.string.english))) {
                    languageToLoad = "en";
                    SavedData.saveLanguage(languageToLoad);
                    Locale locale = new Locale(SavedData.getLanguage());
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getResources().updateConfiguration(config, getResources().getDisplayMetrics());
                } else if (radioButton.getText().toString().equals(getResources().getString(R.string.arabic))) {
                    languageToLoad = "ar";
                    SavedData.saveLanguage(languageToLoad);
                    Locale locale = new Locale(SavedData.getLanguage());
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getResources().updateConfiguration(config, getResources().getDisplayMetrics());

                }
                changeLanguage(languageToLoad,dialog);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void changeLanguage(String languageToLoad, Dialog dialog) {
        String language;
        if (languageToLoad.endsWith("ar")) {
            language = "arabic";
        } else {
            language = "english";
        }
        new JSONParser(this).parseRetrofitRequest(true, ApiClient.getClient().create(ApiInterface.class).updateWebUserLanguage(
                UserDataHelper.getInstance().getUserDataModel().get(0).getUserId(),
                "language"
        ), new Helper() {
            @Override
            public void backResponse(String response) {
                dialog.dismiss();
                Bundle bundle = new Bundle();
                bundle.putString("selected", "home");
                S.I(MainActivity.this, MainActivity.class, bundle);
            }
        });
    }

    private void openPopupLogout() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_logout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialog.getWindow().setAttributes(lp);
        Button btnNo = (Button) dialog.findViewById(R.id.btnNo);
        Button btnYes = (Button) dialog.findViewById(R.id.btnYes);
        dialog.show();
        dialog.setCancelable(false);
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                MyCartHelper.getInstance().delete();
                UserDataHelper.getInstance().delete();
                S.I_clear(MainActivity.this, SplashActivity.class, null);
            }
        });
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag("home");
        if (fragment == null) {
            btnChat.setVisibility(View.VISIBLE);
            titleHome.setVisibility(View.VISIBLE);
            linearBottombar.setVisibility(View.VISIBLE);
            btnBack.setVisibility(View.GONE);
            btnSearch.setVisibility(View.GONE);
            titleService.setVisibility(View.GONE);
            titleNotifications.setVisibility(View.GONE);
            imageCart.setVisibility(View.GONE);
            fragment = fm.findFragmentByTag("home");
            if (fragment == null) {
                FragmentTransaction ft = fm.beginTransaction();
                fragment = new HomeFragment();
                ft.replace(R.id.fragment_frame, fragment, "home");
                ft.commit();
            }
            Drawable unwrappedDrawableHome4 = AppCompatResources.getDrawable(MainActivity.this, R.drawable.ic_home_1_blue);
            Drawable wrappedDrawablePaymentHome4 = DrawableCompat.wrap(unwrappedDrawableHome4);
            DrawableCompat.setTint(wrappedDrawablePaymentHome4, getResources().getColor(R.color.colorBlue));
            imgHome.setImageDrawable(wrappedDrawablePaymentHome4);
            txtNavHome.setTextColor(getResources().getColor(R.color.colorBlue));

            Drawable unwrappedDrawableService4 = AppCompatResources.getDrawable(MainActivity.this, R.drawable.ic_document_gray);
            Drawable wrappedDrawableService4 = DrawableCompat.wrap(unwrappedDrawableService4);
            DrawableCompat.setTint(wrappedDrawableService4, getResources().getColor(R.color.colorTextSecondary));
            imgService.setImageDrawable(wrappedDrawableService4);
            txtService.setTextColor(getResources().getColor(R.color.colorTextSecondary));

            Drawable unwrappedDrawableNotification4 = AppCompatResources.getDrawable(MainActivity.this, R.drawable.ic_bell_gray);
            Drawable wrappedDrawableNotification4 = DrawableCompat.wrap(unwrappedDrawableNotification4);
            DrawableCompat.setTint(wrappedDrawableNotification4, getResources().getColor(R.color.colorTextSecondary));
            imgNotification.setImageDrawable(wrappedDrawableNotification4);
            txtNotification.setTextColor(getResources().getColor(R.color.colorTextSecondary));

            Drawable unwrappedDrawableCart4 = AppCompatResources.getDrawable(MainActivity.this, R.drawable.ic_shopping_cart);
            Drawable wrappedDrawableCart4 = DrawableCompat.wrap(unwrappedDrawableCart4);
            DrawableCompat.setTint(wrappedDrawableCart4, getResources().getColor(R.color.colorTextSecondary));
            imgCart.setImageDrawable(wrappedDrawableCart4);
            txtCart.setTextColor(getResources().getColor(R.color.colorTextSecondary));
        } else {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_logout);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;
            dialog.getWindow().setAttributes(lp);
            TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
            Button btnNo = (Button) dialog.findViewById(R.id.btnNo);
            Button btnYes = (Button) dialog.findViewById(R.id.btnYes);
            tvTitle.setText("Exit");
            dialog.show();
            dialog.setCancelable(false);
            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    FragmentManager fm = getSupportFragmentManager();
                    Fragment fragment = fm.findFragmentByTag("home");
                    if (fragment == null) {
                        FragmentTransaction ft = fm.beginTransaction();
                        fragment = new HomeFragment();
                        ft.replace(R.id.fragment_frame, fragment, "home");
                        ft.commit();
                    }
                }
            });
            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    finishAffinity();
                }
            });
        }
    }
}
