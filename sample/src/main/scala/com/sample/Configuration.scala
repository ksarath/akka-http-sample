package com.sample

import com.typesafe.config.{ Config, ConfigFactory }

trait Configuration {
  val config: Config = defaultConfig

  private lazy val defaultConfig = {
    val fallback = ConfigFactory.defaultReference()
    ConfigFactory.load().withFallback(fallback)
  }
}