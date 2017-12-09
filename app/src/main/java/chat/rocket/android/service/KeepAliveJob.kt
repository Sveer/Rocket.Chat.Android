package chat.rocket.android.service

import chat.rocket.android.RocketChatApplication
import chat.rocket.android.RocketChatCache
import com.evernote.android.job.Job
import com.evernote.android.job.JobManager
import com.evernote.android.job.JobRequest
import java.util.concurrent.TimeUnit

class KeepAliveJob : Job() {

    private val connectivityManager: ConnectivityManagerApi
    private val rocketChatCache: RocketChatCache

    companion object {
        val TAG = "chat.rocket.android.service.KeepAliveJob"
        fun schedule() {
            JobRequest.Builder(TAG)
                    .setExecutionWindow(TimeUnit.SECONDS.toMillis(3L), TimeUnit.SECONDS.toMillis(10L))
                    .setBackoffCriteria(10L, JobRequest.BackoffPolicy.EXPONENTIAL)
                    .setUpdateCurrent(true)
                    .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                    .setRequiresCharging(false)
                    .setRequirementsEnforced(true)
                    .build()
                    .schedule()
        }

        fun cancel() {
            JobManager.instance().cancelAllForTag(TAG)
        }
    }

    init {
        val context = RocketChatApplication.getInstance()
        connectivityManager = ConnectivityManager.getInstance(context)
        rocketChatCache = RocketChatCache(context)
    }

    override fun onRunJob(params: Params): Result {
        connectivityManager.keepAliveServer()
        return Result.SUCCESS
    }
}