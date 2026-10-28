import { useEffect, useState } from 'react'
import Pagination from './Pagination'
import SearchBar from './SearchBar'
import PostItem from './PostItem'
import PostEditor from './PostEditor'
import AuthPanel from './AuthPanel'
import { createPost, fetchMe, logout } from './api'
import './App.css'





const PAGE_SIZE = 3

function App() {
  const [posts, setPosts] = useState([])
  const [page, setPage] = useState(0)
  const [totalPages, setTotalPages] = useState(0)
  const [keyword, setKeyword] = useState('')
  const [loading, setLoading] = useState(true)
  const [reloadCount, setReloadCount] = useState(0)
  const [user, setUser] = useState(null)

  // 화면이 처음 뜰 때 이미 로그인 된 세션이있는지 확인(새로고침해도 로그인 유지)
  useEffect(() => {
    fetchMe().then(setUser).catch(console.error)
  }, [])

  useEffect(() => {
    async function loadPosts() {
      const params = new URLSearchParams({page, size:PAGE_SIZE})
      if(keyword) {
        params.set('keyword', keyword)
      }
      const response = await fetch(`/api/posts?${params}`)
      const data = await response.json()
      setPosts(data.content)
      setTotalPages(data.totalPages)
      setLoading(false)
    }
    loadPosts()
  }, [page, keyword, reloadCount])

  function handleSearch(newKeyword) {
    setKeyword(newKeyword)
    setPage(0)
  }


  async function handleCreate(title, content) {
    await createPost(title, content)
    setPage(0)
    setReloadCount((count) => count + 1)
  }

 
  function handleDeleted() {
    if(posts.length ===1 && page > 0){
      setPage(page-1)
    } else {
      setReloadCount((count) => count + 1)
    }
  }

  function handleUpdated() {
    setReloadCount((count) => count+1)
  }

  function handleCommentsChanged() {
    setReloadCount((count) => count +1)
  }

  async function handleLoggedIn() {
    setUser(await fetchMe())
  }

  async function handleLogout() {
    await logout()
    setUser(null)
  }


  return (
    <main>
      <h1>Spring Board</h1>
      <AuthPanel user={user} onLoggedIn={handleLoggedIn} onLogout={handleLogout} />
      {user && <PostEditor onSubmit={handleCreate}/>}
      <SearchBar onSearch={handleSearch}/>
      {loading && <p>Loading...</p>}
      {!loading && posts.length === 0 && page === 0 &&<p>No posts found.</p>}
      {!loading && posts.length === 0 && page > 0 && (
        <p>
          This page no longer exists.{' '}
          <button onClick={() => setPage(0)}>Go to first Page</button>
        </p>
      )}      
      <ul className="post-list">
        {posts.map((post) => (       
          <PostItem 
            key={post.id} 
            post={post} 
            currentUser={user}
            onDeleted={handleDeleted} 
            onUpdated={handleUpdated} 
            onCommentsChanged={handleCommentsChanged}/>
        ))}
      </ul>
      <Pagination page ={page} totalPages={totalPages} onChange={setPage} />
    </main>
  )
}

export default App