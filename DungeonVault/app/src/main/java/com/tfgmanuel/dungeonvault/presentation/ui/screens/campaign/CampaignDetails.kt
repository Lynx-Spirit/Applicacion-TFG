package com.tfgmanuel.dungeonvault.presentation.ui.screens.campaign

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.tfgmanuel.dungeonvault.R
import com.tfgmanuel.dungeonvault.data.model.Campaign
import com.tfgmanuel.dungeonvault.data.model.User
import com.tfgmanuel.dungeonvault.data.remote.BASE_URL
import com.tfgmanuel.dungeonvault.presentation.ui.components.CustomButtonText
import com.tfgmanuel.dungeonvault.presentation.ui.components.CustomIconButon
import com.tfgmanuel.dungeonvault.presentation.ui.components.DecisionDialog
import com.tfgmanuel.dungeonvault.presentation.ui.components.ItemList
import com.tfgmanuel.dungeonvault.presentation.ui.components.SecondaryTopBar
import com.tfgmanuel.dungeonvault.presentation.ui.components.SimpleCustomContainer
import com.tfgmanuel.dungeonvault.presentation.viewModel.campaignViewModel.CampaignDetailsViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CampaignDetails(modifier: Modifier = Modifier, viewModel: CampaignDetailsViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val isRefreshing = uiState.isLoading
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.forceUpdate() }
    )

    uiState.campaign?.let { campaign ->
        Scaffold(
            modifier = modifier,
            topBar = {
                SecondaryTopBar(
                    onBackClick = {
                        viewModel.goBack()
                    }
                )
            },
            content = { paddingValues ->
                BackgroundImage(paddingValues = paddingValues, imgName = campaign.img_name)

                Content(
                    paddingValues = paddingValues,
                    campaign = campaign,
                    members = uiState.members,
                    permission = uiState.hasPermission,
                    creatorId = uiState.creatorId,
                    deleteDialog = uiState.showDeleteDialog,
                    showDeleteDialog = { viewModel.showDeleteDialog() },
                    hideDeleteDialog = { viewModel.hideDeleteDialog() },
                    kickDialog = uiState.showKickDialog,
                    showKickDialog = { viewModel.showKickDialog() },
                    hideKickDialog = { viewModel.hideKickDialog() },
                    leaveDialog = uiState.showLeaveDialog,
                    showLeaveDialog = { viewModel.showLeaveDialog() },
                    hideLeaveDialog = { viewModel.hideKickDialog() },
                    onStartClick = { viewModel.onStartClick() },
                    onEditClick = { viewModel.onEditClick() },
                    onDeleteClick = { viewModel.onDeleteClick() },
                    onLeaveClick = { viewModel.onAbandonClick() },
                    onKickClick = { userId: Int -> viewModel.onKickClick(userId) },
                    isRefreshing = isRefreshing,
                    pullRefreshState = pullRefreshState
                )
            }
        )
    }
}

@Composable
fun BackgroundImage(paddingValues: PaddingValues, imgName: String) {
    Image(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding()),
        painter = rememberAsyncImagePainter(
            model = "${BASE_URL}files/${imgName}",
            error = painterResource(id = R.drawable.sinimg),
            placeholder = painterResource(id = R.drawable.sinimg)
        ),
        contentDescription = "Imagen de fondo de la camapaña",
        contentScale = ContentScale.Crop
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Content(
    paddingValues: PaddingValues,
    campaign: Campaign,
    creatorId: Int,
    members: List<User>,
    permission: Boolean,
    deleteDialog: Boolean,
    showDeleteDialog: () -> Unit,
    hideDeleteDialog: () -> Unit,
    kickDialog: Boolean,
    showKickDialog: () -> Unit,
    hideKickDialog: () -> Unit,
    leaveDialog: Boolean,
    showLeaveDialog: () -> Unit,
    hideLeaveDialog: () -> Unit,
    onEditClick: () -> Unit,
    onStartClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onLeaveClick: () -> Unit,
    onKickClick: (userId: Int) -> Unit,
    isRefreshing: Boolean,
    pullRefreshState: PullRefreshState
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding())
            .pullRefresh(pullRefreshState)
            .background(Color.Black.copy(alpha = 0.9f)),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = campaign.title,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Bold,
                )

                CampaignButtons(
                    permission = permission,
                    deleteDialog = deleteDialog,
                    showDeleteDialog = { showDeleteDialog() },
                    hideDeleteDialog = { hideDeleteDialog() },
                    leaveDialog = leaveDialog,
                    showLeaveDialog = { showLeaveDialog() },
                    hideLeaveDialog = { hideLeaveDialog() },
                    onStartClick = { onStartClick() },
                    onEditClick = { onEditClick() },
                    onDeleteClick = { onDeleteClick() },
                    onLeaveClick = { onLeaveClick() }
                )

                InviteCodeBox(inviteCode = campaign.invite_code)

                Spacer(modifier = Modifier.height(10.dp))

                CampaignInformation(description = campaign.description)

                Spacer(modifier = Modifier.height(10.dp))

                MemberList(
                    members = members,
                    onKickClick = { userId: Int -> onKickClick(userId) },
                    permission = permission,
                    creatorId = creatorId,
                    dialog = kickDialog,
                    hideDialog = { hideKickDialog() },
                    showDialog = { showKickDialog() }
                )
            }
        }
        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@Composable
fun CampaignButtons(
    permission: Boolean,
    deleteDialog: Boolean,
    showDeleteDialog: () -> Unit,
    hideDeleteDialog: () -> Unit,
    leaveDialog: Boolean,
    showLeaveDialog: () -> Unit,
    hideLeaveDialog: () -> Unit,
    onStartClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onLeaveClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .wrapContentHeight()
            .padding(10.dp)
            .background(
                color = Color(0xFF131313),
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            CustomButtonText(
                modifier = Modifier.fillMaxWidth(),
                text = "COMENZAR",
                fontSize = 18.sp,
                onClick = { onStartClick() }
            )

            Spacer(modifier = Modifier.height(5.dp))
            if (permission) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CustomIconButon(
                        modifier = Modifier.weight(1f),
                        buttonColor = Color.Transparent,
                        icon = Icons.Default.Edit,
                        text = "EDITAR",
                        onClick = { onEditClick() }
                    )

                    VerticalDivider(modifier = Modifier.height(50.dp))

                    CustomIconButon(
                        modifier = Modifier.weight(1f),
                        buttonColor = Color.Transparent,
                        icon = Icons.Default.Delete,
                        textColor = Color(0xFFF44336),
                        text = "ELIMINAR",
                        onClick = { showDeleteDialog() }
                    )
                }

                if (deleteDialog) {
                    DecisionDialog(
                        onDismissRequest = {
                            hideDeleteDialog()
                        },
                        onConfirmation = {
                            hideDeleteDialog()
                            onDeleteClick()
                        },
                        dialogTitle = "Eliminar campaña",
                        dialogText = "¿Estás seguro de que quieres eliminar esta camapaña? Esta acción no se puede deshacer."
                    )
                }
            } else {
                CustomIconButon(
                    buttonColor = Color.Transparent,
                    icon = Icons.AutoMirrored.Filled.ExitToApp,
                    textColor = Color(0xFFF44336),
                    text = "ABANDONAR CAMPAÑA",
                    onClick = { showLeaveDialog() }
                )

                if (leaveDialog) {
                    DecisionDialog(
                        onDismissRequest = {
                            hideLeaveDialog()
                        },
                        onConfirmation = {
                            hideLeaveDialog()
                            onLeaveClick()
                        },
                        dialogTitle = "Abandonar campaña",
                        dialogText = "¿Estás seguro de que quieres abandonar esta camapaña? Esta acción no se puede deshacer."
                    )
                }
            }
        }
    }
}

@Composable
fun MemberList(
    members: List<User>,
    permission: Boolean,
    dialog: Boolean,
    creatorId: Int,
    showDialog: () -> Unit,
    hideDialog: () -> Unit,
    onKickClick: (userId: Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(200.dp)
            .background(
                color = Color(0xFF131313),
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = "Miembros de la partida",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(3.dp))

            ItemList(
                modifier = Modifier.fillMaxSize(),
                tList = members,
                text = "Error al mostrar los usuarios",
                fontSize = 20.sp
            ) { user: User ->
                SimpleCustomContainer(
                    modifier = Modifier
                        .height(50.dp),
                    title = user.nickname,
                    imgName = user.avatar,
                    placeholder = R.drawable.default_avatar,
                    contentDescription = "Avatar usuario",
                    onClick = { showDialog() },
                    permission = creatorId != user.id  && permission
                )

                if (dialog) {
                    DecisionDialog(
                        onConfirmation = {
                            hideDialog()
                            onKickClick(user.id)
                        },
                        onDismissRequest = {
                            hideDialog()
                        },
                        dialogTitle = "Echar usuario?",
                        dialogText = "¿Estás seguro de que quieres echar a ${user.nickname} de esta camapaña? Esta acción no se puede deshacer."
                    )
                }
            }
        }
    }
}

@Composable
fun CampaignInformation(description: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .wrapContentHeight()
            .background(
                color = Color(0xFF131313),
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = "Descripcion",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = description,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
fun InviteCodeBox(inviteCode: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .wrapContentHeight()
            .background(
                color = Color(0xFF131313),
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = "Código de invitación",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = inviteCode,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}