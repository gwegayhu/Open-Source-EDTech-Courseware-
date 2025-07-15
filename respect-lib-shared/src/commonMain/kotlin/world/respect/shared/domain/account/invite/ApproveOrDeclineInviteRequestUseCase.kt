package world.respect.shared.domain.account.invite


/**
 * Could take the accountmanager as a dependency: any API calls need to be linked to a specific
 * account
 */
interface ApproveOrDeclineInviteRequestUseCase {

    /**
     * @param pendingInviteUid as Per RespectPendingInviteState.uid
     * @param approved true/false
     */
    suspend operator fun invoke(
        pendingInviteUid: String,
        approved: Boolean,
        deciderSourcedId: String,
    )

}