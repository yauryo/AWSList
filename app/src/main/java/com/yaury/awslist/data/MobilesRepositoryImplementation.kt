package com.yaury.awslist.data

import com.yaury.awslist.model.MobileItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MobilesRepositoryImplementation(
    private val mobileApi: MobileApi
): MobilesRepository {
    override suspend fun getMobilesList(): Flow<Result<List<MobileItem>>> {
        return flow {
            val mobilesFromApi = try {
                mobileApi.getMobiles()
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.Error(message = "An unknown error occurred"))
                return@flow
            }
            emit(Result.Success(mobilesFromApi))
        }
    }
}