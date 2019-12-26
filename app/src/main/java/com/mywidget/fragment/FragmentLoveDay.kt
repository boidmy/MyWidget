package com.mywidget.fragment

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.mywidget.ApiConnection
import com.mywidget.MainApplication
import com.mywidget.R
import com.mywidget.Util
import com.mywidget.data.LmemoData
import com.mywidget.lmemo.Ldata
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.main_fragment_fragment2.view.*
import kotlinx.android.synthetic.main.memo_list_dialog.view.*
import kotlinx.android.synthetic.main.memo_list_dialog_item.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*




class FragmentLoveDay : Fragment(), View.OnClickListener  {

    private var unSubscripbe: CompositeDisposable? = null

    private var database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Lmemo")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onDestroy() {
        super.onDestroy()

        unSubscripbe?.dispose()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.main_fragment_fragment2, container, false)

        //view.heart_day.text = Util.date().toString() + "일"

        try {
            val date = SimpleDateFormat("yyyyMMdd").parse(MainApplication.loveDaySelect())
            val cal: Calendar = Calendar.getInstance()
            cal.time = date
            val year: Int = cal.get(Calendar.YEAR)
            val month: Int = cal.get(Calendar.MONTH) + 1
            val nowdate: Int = cal.get(Calendar.DAY_OF_MONTH)
            val dayOfWeek: Int = cal.get(Calendar.DAY_OF_WEEK)

            view.heart_day.text = Util.loveDay(year, month, nowdate).toString() + "일"
        } catch (e: Exception) {
            view.heart_day.text = "0일"
        }

        view.left_container.setOnClickListener(this)
        view.right_container.setOnClickListener(this)

        selectMemo(view)

        return view
    }

    fun selectMemo(view: View) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(data: DataSnapshot) {
                var arrayRight = arrayListOf<Ldata>()
                var arrayLeft = arrayListOf<Ldata>()

                for (ds in data.child("콩이").children) {
                    val product = ds.getValue(Ldata::class.java)
                    arrayRight.add(product!!)
                }

                for (ds in data.child("뿡이").children) {
                    val product = ds.getValue(Ldata::class.java)
                    arrayLeft.add(product!!)
                }

                if(data.child("뿡이").value != null) {
                    view.lmemo_left.text = arrayLeft[arrayLeft.size-1].memo
                    view.date_left.text = arrayLeft[arrayLeft.size-1].date
                }

                if(data.child("콩이").value != null) {
                    view.lmemo_right.text = arrayRight[arrayRight.size-1].memo
                    view.date_right.text = arrayRight[arrayRight.size-1].date
                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.left_container -> {
                unSubscripbe?.add(ApiConnection.Instance().retrofitService
                    .lmemoData2("뿡이")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe (
                        { item ->
                            memoDialog(item)
                        }, {exception ->
                            Log.d("error!", exception.toString())
                        })
                )

            }
            R.id.right_container -> {
                unSubscripbe?.add(ApiConnection.Instance().retrofitService
                    .lmemoData2("콩이")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe (
                        { item ->
                            memoDialog(item)
                        }, {exception ->
                            Log.d("error!", exception.toString())
                        })
                )
            }
        }
    }

    fun memoDialog(jsonObject: JsonObject) {
        val obj = JSONObject(Gson().toJson(jsonObject))
        val x = obj.keys()
        val array = JSONArray()

        while (x.hasNext()) {
            val key = x.next() as String
            array.put(obj.get(key))
        }

        val arrayLmemoData: ArrayList<LmemoData> = Gson().fromJson(array.toString(), object:TypeToken<ArrayList<LmemoData>>(){}.type)

        val view: View = layoutInflater.inflate(R.layout.memo_list_dialog, null)
        val popupWindow = PopupWindow(
            view,
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
        for (ds in arrayLmemoData) {
            val listItem: View = layoutInflater.inflate(R.layout.memo_list_dialog_item, null)
            listItem.memo.text = ds.memo
            listItem.date.text = ds.date
            view.memo_list_container.addView(listItem)

        }
    }

    companion object {
        private val ARG_TAB_POS = "tab_position"

        fun newInstance(position: Int): FragmentLoveDay {
            val fragment = FragmentLoveDay()
            val args = Bundle()
            args.putInt(ARG_TAB_POS, position)
            fragment.arguments = args
            return fragment
        }
    }

}
