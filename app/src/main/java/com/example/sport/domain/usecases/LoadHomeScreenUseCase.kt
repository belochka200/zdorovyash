package com.example.sport.domain.usecases

private interface LoadHomeScreenUseCase {

    /**
     * @return Pair<String, String>, где первое значение - температура, второе - текущие осадки
     * */
    suspend fun loadWeather(): Pair<Int, String> // todo: заменить String на класс осадков
}

class LoadHomeScreenUseCaseImpl : LoadHomeScreenUseCase {
    override suspend fun loadWeather(): Pair<Int, String> {
        return Pair(15, "Солнечно")
    }
}