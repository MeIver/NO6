<template>
	<div>
		<div class="center_view">
			<div class="list_search_view">
				<el-form :model="searchQuery" class="search_form" >
					<div class="search_view">
						<div class="search_label">
							用户账号：
						</div>
						<div class="search_box">
							<el-input class="search_inp" v-model="searchQuery.zhanghao" placeholder="用户账号"
								clearable>
							</el-input>
						</div>
					</div>
					<div class="search_view">
						<div class="search_label">
							手机号：
						</div>
						<div class="search_box">
							<el-input class="search_inp" v-model="searchQuery.shouji" placeholder="手机号"
								clearable>
							</el-input>
						</div>
					</div>
					<div class="search_view">
						<div class="search_label">
							发送状态：
						</div>
						<div class="search_box">
							<el-select v-model="searchQuery.fasonZhuangtai" placeholder="请选择发送状态" clearable>
								<el-option label="成功" value="成功"></el-option>
								<el-option label="失败" value="失败"></el-option>
							</el-select>
						</div>
					</div>
					<div class="search_view">
						<div class="search_label">
							处理状态：
						</div>
						<div class="search_box">
							<el-select v-model="searchQuery.chuliZhuangtai" placeholder="请选择处理状态" clearable>
								<el-option label="未处理" value="未处理"></el-option>
								<el-option label="已处理" value="已处理"></el-option>
								<el-option label="已忽略" value="已忽略"></el-option>
							</el-select>
						</div>
					</div>
					<div class="search_btn_view">
						<el-button class="search_btn" type="primary" @click="searchClick()" size="small">搜索</el-button>
					</div>
				</el-form>
				<div class="btn_view">
					<el-button class="del_btn" type="danger" :disabled="selRows.length?false:true" @click="delClick(null)"  v-if="btnAuth('tongzhijilu','删除')">
						<i class="iconfont icon-shanchu4"></i>
						删除
					</el-button>
					<el-button class="add_btn" type="warning" @click="retryClick()" :disabled="selRows.length?false:true">
						<i class="iconfont icon-xinzeng1"></i>
						批量重试
					</el-button>
				</div>
			</div>
			<el-table
				v-loading="listLoading"
				border
				:stripe='false'
				@selection-change="handleSelectionChange"
				ref="table"
				v-if="btnAuth('tongzhijilu','查看')"
				:data="list"
				@row-click="listChange">
				<el-table-column :resizable='true' align="left" header-align="left" type="selection" width="55" />
				<el-table-column label="序号" width="70" :resizable='true' align="left" header-align="left">
					<template #default="scope">{{ (listQuery.page-1)*listQuery.limit+scope.$index + 1}}</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="zhanghao"
					label="用户账号">
					<template #default="scope">
						{{scope.row.zhanghao}}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="shouji"
					label="手机号">
					<template #default="scope">
						{{scope.row.shouji}}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="tongzhiLeixing"
					label="通知类型">
					<template #default="scope">
						{{scope.row.tongzhiLeixing}}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="fasonShijian"
					label="发送时间">
					<template #default="scope">
						{{scope.row.fasonShijian}}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="fasonZhuangtai"
					label="发送状态">
					<template #default="scope">
						<el-tag :type="scope.row.fasonZhuangtai === '成功' ? 'success' : 'danger'">
							{{scope.row.fasonZhuangtai}}
						</el-tag>
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="chongshiCishu"
					label="重试次数">
					<template #default="scope">
						{{scope.row.chongshiCishu}}
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="chuliZhuangtai"
					label="处理状态">
					<template #default="scope">
						<el-tag :type="getChuliZhuangtaiType(scope.row.chuliZhuangtai)">
							{{scope.row.chuliZhuangtai}}
						</el-tag>
					</template>
				</el-table-column>
				<el-table-column min-width="140"
					:resizable='true'
					:sortable='true'
					align="left"
					header-align="left"
					prop="shibaiYuanyin"
					label="失败原因">
					<template #default="scope">
						<el-tooltip v-if="scope.row.shibaiYuanyin" :content="scope.row.shibaiYuanyin" placement="top">
							<span class="ellipsis-text">{{scope.row.shibaiYuanyin}}</span>
						</el-tooltip>
						<span v-else>-</span>
					</template>
				</el-table-column>
				<el-table-column label="操作" width="300" :resizable='true' :sortable='true' align="left" header-align="left">
					<template #default="scope">
						<el-button class="view_btn" type="info" v-if=" btnAuth('tongzhijilu','查看')" @click="infoClick(scope.row.id)">
							<i class="iconfont icon-sousuo2"></i>
							查看
						</el-button>
						<el-button class="edit_btn" type="warning" @click="handleClick(scope.row)" v-if="scope.row.fasonZhuangtai === '失败' && scope.row.chuliZhuangtai === '未处理'">
							<i class="iconfont icon-xiugai5"></i>
							处理
						</el-button>
						<el-button class="del_btn" type="danger" @click="delClick(scope.row.id)"  v-if="btnAuth('tongzhijilu','删除')">
							<i class="iconfont icon-shanchu4"></i>
							删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<el-pagination
				background
				:layout="layouts.join(',')"
				:total="total"
				:page-size="listQuery.limit"
                v-model:current-page="listQuery.page"
				prev-text="上一页"
				next-text="下一页"
				:hide-on-single-page="false"
				:style='{}'
				:page-sizes="[10, 20, 30, 40, 50, 100]"
				@size-change="sizeChange"
				@current-change="currentChange"  />
		</div>
		<formModel ref="formRef" @formModelChange="formModelChange"></formModel>
		<!-- 处理对话框 -->
		<el-dialog title="处理失败通知" v-model="handleDialogVisible" width="500px">
			<el-form :model="handleForm" label-width="100px">
				<el-form-item label="处理状态">
					<el-select v-model="handleForm.chuliZhuangtai" placeholder="请选择处理状态">
						<el-option label="已处理" value="已处理"></el-option>
						<el-option label="已忽略" value="已忽略"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="处理备注">
					<el-input type="textarea" v-model="handleForm.chuliBeizhu" rows="4" placeholder="请输入处理备注"></el-input>
				</el-form-item>
			</el-form>
			<template #footer>
				<el-button @click="handleDialogVisible = false">取消</el-button>
				<el-button type="primary" @click="submitHandle">确定</el-button>
			</template>
		</el-dialog>
	</div>
</template>
<script setup>
	import axios from 'axios'
    import moment from "moment"
	import {
		reactive,
		ref,
		getCurrentInstance,
		nextTick,
		onMounted,
		watch,
		computed,
	} from 'vue'
	import {
		useRoute,
		useRouter
	} from 'vue-router'
	import {
		ElMessageBox,
		ElMessage
	} from 'element-plus'
	import {
		useStore
	} from 'vuex';
	const store = useStore()
	const user = computed(()=>store.getters['user/session'])
	const avatar = ref(store.state.user.avatar)
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	import formModel from './formModel.vue'
	//基础信息

	const tableName = 'tongzhijilu'
	const formName = '通知记录'
	const route = useRoute()
	//基础信息
	onMounted(()=>{
	})
	//列表数据
	const list = ref(null)
	const table = ref(null)
	const listQuery = ref({
		page: 1,
		limit: 10,
		sort: 'id',
		order: 'desc'
	})
	const searchQuery = ref({})
	const selRows = ref([])
	const listLoading = ref(false)
	const listChange = (row) =>{
		nextTick(()=>{
			//table.value.clearSelection()
			table.value.toggleRowSelection(row)
		})
	}
	//列表
	const getList = () => {
		listLoading.value = true
		let params = JSON.parse(JSON.stringify(listQuery.value))
		params['sort'] = 'id'
		params['order'] = 'desc'
		if(searchQuery.value.zhanghao&&searchQuery.value.zhanghao!=''){
			params['zhanghao'] = '%' + searchQuery.value.zhanghao + '%'
		}
		if(searchQuery.value.shouji&&searchQuery.value.shouji!=''){
			params['shouji'] = '%' + searchQuery.value.shouji + '%'
		}
		if(searchQuery.value.fasonZhuangtai&&searchQuery.value.fasonZhuangtai!=''){
			params['fasonZhuangtai'] = searchQuery.value.fasonZhuangtai
		}
		if(searchQuery.value.chuliZhuangtai&&searchQuery.value.chuliZhuangtai!=''){
			params['chuliZhuangtai'] = searchQuery.value.chuliZhuangtai
		}
		context.$http({
			url: `${tableName}/page`,
			method: 'get',
			params: params
		}).then(res => {
			listLoading.value = false
			list.value = res.data.data.list
			total.value = Number(res.data.data.total)
		})
	}
	//删
	const delClick = (id) => {
		let ids = ref([])
		if (id) {
			ids.value = [id]
		} else {
			if (selRows.value.length) {
				for (let x in selRows.value) {
					ids.value.push(selRows.value[x].id)
				}
			} else {
				return false
			}
		}
		ElMessageBox.confirm(`是否删除选中${formName}`, '提示', {
			confirmButtonText: '是',
			cancelButtonText: '否',
			type: 'warning',
		}).then(() => {
			context.$http({
				url: `${tableName}/delete`,
				method: 'post',
				data: ids.value
			}).then(res => {
				context?.$toolUtil.message('删除成功', 'success',()=>{
					getList()
				})
			})
		}).catch(_ => {})
	}
	//多选
	const handleSelectionChange = (e) => {
		selRows.value = e
	}
	//列表数据
	//分页
	const total = ref(0)
	const layouts = ref(["total","prev","pager","next","sizes","jumper"])
	const sizeChange = (size) => {
		listQuery.value.limit = size
		getList()
	}
	const currentChange = (page) => {
		listQuery.value.page = page
		getList()
	}
	//分页
	//权限验证
	const btnAuth = (e,a)=>{
		return context?.$toolUtil.isAuth(e,a)
	}
	//搜索
	const searchClick = () => {
		listQuery.value.page = 1
		getList()
	}
	//表单
	const formRef = ref(null)
	const formModelChange=()=>{
		searchClick()
	}
	const addClick = ()=>{
		formRef.value.init()
	}
	const editClick = (id=null)=>{
		if(id){
			formRef.value.init(id,'edit')
			return
		}
		if(selRows.value.length){
			formRef.value.init(selRows.value[0].id,'edit')
		}
	}

	const infoClick = (id=null)=>{
		if(id){
			formRef.value.init(id,'info')
		}
		else if(selRows.value.length){
			formRef.value.init(selRows.value[0].id,'info')
		}
	}
	// 表单

	// 获取处理状态标签类型
	const getChuliZhuangtaiType = (status) => {
		switch(status) {
			case '已处理': return 'success'
			case '未处理': return 'warning'
			case '已忽略': return 'info'
			default: return ''
		}
	}

	// 处理对话框
	const handleDialogVisible = ref(false)
	const handleForm = ref({
		id: null,
		chuliZhuangtai: '已处理',
		chuliBeizhu: ''
	})
	const currentRow = ref(null)

	// 处理按钮点击
	const handleClick = (row) => {
		currentRow.value = row
		handleForm.value = {
			id: row.id,
			chuliZhuangtai: '已处理',
			chuliBeizhu: ''
		}
		handleDialogVisible.value = true
	}

	// 提交处理
	const submitHandle = () => {
		if (!handleForm.value.chuliZhuangtai) {
			ElMessage.warning('请选择处理状态')
			return
		}
		context.$http({
			url: `${tableName}/handle`,
			method: 'post',
			data: handleForm.value
		}).then(res => {
			ElMessage.success('处理成功')
			handleDialogVisible.value = false
			getList()
		})
	}

	// 批量重试
	const retryClick = () => {
		if (!selRows.value.length) {
			ElMessage.warning('请选择要重试的记录')
			return
		}
		const failedRows = selRows.value.filter(row => row.fasonZhuangtai === '失败')
		if (!failedRows.length) {
			ElMessage.warning('选中的记录中没有失败的通知')
			return
		}
		ElMessageBox.confirm(`确定要重试选中的 ${failedRows.length} 条失败通知吗？`, '提示', {
			confirmButtonText: '确定',
			cancelButtonText: '取消',
			type: 'warning',
		}).then(() => {
			let successCount = 0
			let failCount = 0
			const promises = failedRows.map(row => {
				return context.$http({
					url: `notification/retry/${row.id}`,
					method: 'post'
				}).then(res => {
					if (res.data.code === 0) {
						successCount++
					} else {
						failCount++
					}
				}).catch(() => {
					failCount++
				})
			})
			Promise.all(promises).then(() => {
				ElMessage.success(`重试完成：成功 ${successCount} 条，失败 ${failCount} 条`)
				getList()
			})
		}).catch(_ => {})
	}

	//初始化
	const init = () => {
		getList()
	}
	init()
</script>
<style lang="scss" scoped>
	.ellipsis-text {
		display: inline-block;
		max-width: 200px;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}

	// 操作盒子
	.list_search_view {
		// 搜索盒子
		.search_form {
			// 子盒子
			.search_view {
				// 搜索label
				.search_label {
				}
				// 搜索item
				.search_box {
					// 输入框
					:deep(.search_inp) {
					}
				}
			}
			// 搜索按钮盒子
			.search_btn_view {
				// 搜索按钮
				.search_btn {
				}
				// 搜索按钮-悬浮
				.search_btn:hover {
				}
			}
		}
		//头部按钮盒子
		.btn_view {
			// 其他
			:deep(.el-button--default){
			}
			// 其他-悬浮
			:deep(.el-button--default:hover){
			}
			// 新增
			:deep(.el-button--success){
			}
			// 新增-悬浮
			:deep(.el-button--success:hover){
			}
			// 删除
			:deep(.el-button--danger){
			}
			// 删除-悬浮
			:deep(.el-button--danger:hover){
			}
			// 统计
			:deep(.el-button--warning){
			}
			// 统计-悬浮
			:deep(.el-button--warning:hover){
			}
		}
	}
	// 表格样式
	.el-table {
		:deep(.el-table__header-wrapper) {
			thead {
				tr {
					th {
						.cell {
						}
					}
				}
			}
		}
		:deep(.el-table__body-wrapper) {
			tbody {
				tr {
					td {
						.cell {
							// 编辑
							.el-button--primary {
							}
							// 编辑-悬浮
							.el-button--primary:hover {
							}
							// 详情
							.el-button--info {
							}
							// 详情-悬浮
							.el-button--info:hover {
							}
							// 删除
							.el-button--danger {
							}
							// 删除-悬浮
							.el-button--danger:hover {
							}
							// 跨表
							.el-button--success {
							}
							// 跨表-悬浮
							.el-button--success:hover {
							}
							// 操作
							.el-button--warning {
							}
							// 操作-悬浮
							.el-button--warning:hover {
							}
						}
					}
				}
				tr:hover {
					td {
					}
				}
			}
		}
	}
	// 分页器
	.el-pagination {
		// 总页码
		:deep(.el-pagination__total) {
		}
		// 上一页
		:deep(.btn-prev) {
		}
		// 下一页
		:deep(.btn-next) {
		}
		// 上一页禁用
		:deep(.btn-prev:disabled) {
		}
		// 下一页禁用
		:deep(.btn-next:disabled) {
		}
		// 页码
		:deep(.el-pager) {
			// 数字
			.number {
			}
			// 数字悬浮
			.number:hover {
			}
			// 选中
			.number.is-active {
			}
		}
		// sizes
		:deep(.el-pagination__sizes) {
			display: inline-block;
			vertical-align: top;
			font-size: 13px;
			line-height: 28px;
			height: 28px;
			.el-select {
			}
		}
		// 跳页
		:deep(.el-pagination__jump) {
			// 输入框
			.el-input {
			}
		}
	}
</style>
