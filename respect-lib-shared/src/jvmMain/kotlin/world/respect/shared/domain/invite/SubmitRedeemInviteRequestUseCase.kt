package world.respect.shared.domain.invite

import world.respect.datalayer.respect.model.invite.RespectRedeemInviteRequest


/**
 * Submits a RespectRedeemInviteRequest . This results in the creation of a temporary account
 * awaiting activation.
 *
 * Invite handling flow is as follows:
 * 1) Client sends invite code to systemurl; which then delegates to a specific server as per
 *    code (e.g. code starts with AE goes to country server for AE, etc). The client receives a
 *    RespectInviteInfo response.
 * 2) User fills in required information and submits a RespectRedeemInviteRequest
 * 3) New temporary account is created awaiting approval. User goes to WaitForApproval screen
 * 4) Teacher or admin approves the RespectRedeemInviteRequest by calling a REST API
 * 5) Server generates OneRosterUser and OneRosterEnrolment entities in database. Response to
 *    endpoint call by client includes the new entities, which the client then inserts (updating
 *    what the teacher sees on the class screen)
 */
interface SubmitRedeemInviteRequestUseCase {

    suspend operator fun invoke(redeemRequest: RespectRedeemInviteRequest)

}