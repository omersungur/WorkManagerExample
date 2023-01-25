package com.omersungur.workmanagerexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.work.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val data = Data.Builder().putInt("intKey",1).build() // verimizi yolladık.

        val constraints = Constraints.Builder() // Sistem ile ilgili kısıtlayıcı parametrelerimizi verebiliriz.
            //.setRequiredNetworkType(NetworkType.CONNECTED) // İnternet bağlantısını zorunlu kılıyoruz.
            //.setRequiresCharging(true) // İşlemin yapılabilmesi için sistemin şarj oluyor olması gerekmektedir.
            .build()

        /*val myWorkRequest : WorkRequest = OneTimeWorkRequestBuilder<RefreshDatabase>() // Sadece bir kere istek attığımız yapı
            //.setConstraints(constraints) // kısıtlamaların yer aldığı değişkenimizi burada entegre ediyoruz.
            .setInputData(data) // data kısmını buradan gönderdik.
            //.setInitialDelay(5,TimeUnit.HOURS) // gecikmeli başlatma için kullanılır.
            //.addTag("") // Birden fazla work varsa ayrıştırmak için kullanırız.
            .build()*/

        val myWorkRequest : PeriodicWorkRequest = PeriodicWorkRequestBuilder<RefreshDatabase>(15,TimeUnit.MINUTES)
            .setInputData(data)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueue(myWorkRequest)

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(myWorkRequest.id).observe(this,
            Observer {
                if (it.state == WorkInfo.State.SUCCEEDED) {
                    println("succeded")

                }else if (it.state == WorkInfo.State.FAILED) {
                    println("failed :/")

                } else if (it.state == WorkInfo.State.RUNNING) {
                    println("running")
                }
            })

        //Chaining > workManager içindeki workları zincirleme şekilde yapabiliriz

        /*
        val oneTimeWorkRequest: OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<RefreshDatabase>()
                .setConstraints(constraints)
                .setInputData(data)
                //.setInitialDelay(5,TimeUnit.HOURS)
                //.addTag("myTag")
                .build()
         WorkManager.getInstance(this).beginWith(oneTimeWorkRequest) //ilk bu work ile başla
             .then(oneTimeWorkRequest) // sonra bunu yap
             .then(oneTimeWorkRequest) // daha sonra da bunu yap
             .enqueue() // asenkron yapı olduğunu belirtiyoruz.
         */

        //WorkManager.getInstance(this).cancelAllWork() // workmanager içindeki workları iptal edebiliriz.
    }
}