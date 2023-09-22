package com.example.starwarsjunior.data.personage

import com.example.starwarsjunior.data.common.ResultWrapper
import com.example.starwarsjunior.data.personage.objects.Personage
import com.example.starwarsjunior.data.personage.objects.PersonageListResponse

interface IPersonageDataSource {
    //Interfaces required for all objects in this data source.
    interface Common {

    }

    //Interfaces specific to remote data source
    interface Remote : Common {
        suspend fun getPersonages() : ResultWrapper<PersonageListResponse>
    }

    //interfaces specific to local data source
    interface Local : Common {

    }

    //interfaces specific to the main repository object. (cache operations, for example). Inherits both Remote and Local as those are accessed by use cases via the repository.
    interface Main : Remote, Local {
        suspend fun getCachedPersonage(personageID: Int) : ResultWrapper<Personage?>
    }
}