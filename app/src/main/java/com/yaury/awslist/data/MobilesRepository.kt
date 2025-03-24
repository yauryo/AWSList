package com.yaury.awslist.data

import com.yaury.awslist.model.MobileItem
import kotlinx.coroutines.flow.Flow

interface MobilesRepository {
    suspend fun getMobilesList(): Flow<Result<List<MobileItem>>>

}