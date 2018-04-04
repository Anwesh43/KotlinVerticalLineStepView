package ui.anwesome.com.kotlinverticallinestepview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import ui.anwesome.com.verticallineview.VerticalLineStepView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        VerticalLineStepView.create(this)
    }
}
