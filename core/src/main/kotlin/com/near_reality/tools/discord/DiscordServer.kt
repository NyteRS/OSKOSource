package com.near_reality.tools.discord
 import dev.kord.common.entity.Snowflake

/**
 * Represents a discord server.
 *
 * @author Stan van der Bend
 */
 sealed class DiscordServer(id: Long) {

    /**
     * The id of this server, or guild in discord terminology.
     */
     val guildId = Snowflake(1334594125764362242)

    /**
     * The [id][Snowflake] of the general chat channel.
     */
     abstract val generalChannelId: Snowflake

    /**
     * The [id][Snowflake] of the broadcast channel.
     */
     abstract val broadcastChannelId: Snowflake

     /**
      * A [DiscordServer] in which logs are send and broadcasts for the beta world.
      */
     data object Staff : DiscordServer(id = 1334594125764362242) {

         override val generalChannelId = Snowflake(value =1378466375294521374)
         override val broadcastChannelId = Snowflake(value =1378466375294521374)

         val economySearchChannelId = Snowflake(1378466375294521374)
         val automatedDetectionChannelId = Snowflake(1378466375294521374)
         val modelChannelId = Snowflake(1378466375294521374)
         val developerRoleId = Snowflake(1357205559719231648)
         val managerRoleId = Snowflake(1352296385822851182)
     }

     /**
      * The main community [DiscordServer].
      */
     object Main : DiscordServer(id = 1335646083845455974) {

         override val generalChannelId = Snowflake(value = 1378466375294521374)
         override val broadcastChannelId = Snowflake(value = 1378466375294521374)
     }
 }
