package world.respect.datalayer.oneroster

import world.respect.datalayer.oneroster.rostering.OneRosterRosterDataSource

/**
 * As per the spec (section 2):
 * https://www.imsglobal.org/spec/oneroster/v1p2
 *
 * There are three services: Rostering Service, Gradebook Service, and Resource Service.
 *
 * The Rostering Service is read-only: RESPECT users can either:
 *  a) Use the respect-server app API such that authorized users can write data (e.g. use the app to
 *     invite students to a class)
 *  or
 *  b) Use a different upstream rostering service (any server that implements OneRoster 1.2 rostering
 *     service), in which case the rostering information editing (eg. enrolments, adding users, etc)
 *     must be performed using the upstream service.
 */
interface OneRosterDataSource {

    val rosterService: OneRosterRosterDataSource

}