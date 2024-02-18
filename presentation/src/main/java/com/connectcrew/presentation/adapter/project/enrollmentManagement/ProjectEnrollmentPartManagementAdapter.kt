package com.connectcrew.presentation.adapter.project.enrollmentManagement

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.parseAsHtml
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.ItemProjectEnrollmentPartManagementBinding
import com.connectcrew.presentation.model.project.ProjectEnrollmentPartMember
import com.connectcrew.presentation.model.project.ProjectEnrollmentState
import com.connectcrew.presentation.util.TimeUtil
import com.connectcrew.presentation.util.executeAfter
import com.connectcrew.presentation.util.listener.setOnSingleClickListener

class ProjectEnrollmentPartManagementAdapter(
    private val onSelectCopyContact: (String) -> Unit,
    private val onChangePassState: (Boolean, Pair<Int, String>) -> Unit,
    private val onSelectedProfile: (Int) -> Unit
) : ListAdapter<ProjectEnrollmentPartMember, ProjectEnrollmentPartManagementViewHolder>(
    object : DiffUtil.ItemCallback<ProjectEnrollmentPartMember>() {
        override fun areItemsTheSame(oldItem: ProjectEnrollmentPartMember, newItem: ProjectEnrollmentPartMember): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ProjectEnrollmentPartMember, newItem: ProjectEnrollmentPartMember): Boolean {
            return oldItem.user.id == newItem.user.id
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectEnrollmentPartManagementViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ProjectEnrollmentPartManagementViewHolder(ItemProjectEnrollmentPartManagementBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ProjectEnrollmentPartManagementViewHolder, position: Int) {
        val uiModel = getItem(position) ?: return

        holder.binding.executeAfter {
            projectEnrollmentPartMember = uiModel
            tvUserPart.text = uiModel.user.parts.joinToString()
            tvUserResultDate.text = when (uiModel.state) {
                ProjectEnrollmentState.ACCEPT -> root.context.resources.getString(R.string.project_enrollments_part_date_for_pass, TimeUtil.getDateTimeFormatString(uiModel.enrollmentAt, root.context.resources.getString(R.string.date_format_time_date)))
                ProjectEnrollmentState.REJECT -> root.context.resources.getString(R.string.project_enrollments_part_date_for_reject, TimeUtil.getDateTimeFormatString(uiModel.enrollmentAt, root.context.resources.getString(R.string.date_format_time_date)))
                else -> ""
            }.parseAsHtml()

            cvUserProfile.setOnSingleClickListener { onSelectedProfile(uiModel.user.id) }
            llUserContact.setOnSingleClickListener { onSelectCopyContact(uiModel.contact) }
            btnReject.setOnSingleClickListener { onChangePassState(false, uiModel.applyId to uiModel.user.nickname) }
            btnPass.setOnSingleClickListener { onChangePassState(true, uiModel.applyId to uiModel.user.nickname) }
        }
    }

    override fun getItemViewType(position: Int) = R.layout.item_project_enrollment_part_management
}