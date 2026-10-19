import { useEffect, useState } from 'react'
import Pagination from './Pagination'
import SearchBar from './SearchBar'
import PostItem from './PostItem'
import './App.css'




const PAGE_SIZE = 3

function App() {
  const [posts, setPosts] = useState([])
  const [page, setPage] = useState(0)
  const [totalPages, setTotalPages] = useState(0)
  const [keyword, setKeyword] = useState('')
  const [loading, setLoading] = useState(true)

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
  }, [page, keyword])

  function handleSearch(newKeyword) {
    setKeyword(newKeyword)
    setPage(0)
  }

  return (
    <main>
      <h1>Spring Board</h1>
      <SearchBar onSearch={handleSearch}/>
      {loading && <p>Loading...</p>}
      {!loading && posts.length === 0 && <p>No posts found.</p>}
      <ul className="post-list">
        {posts.map((post) => (
          <PostItem key={post.id} post={post}/>
        ))}
      </ul>
      <Pagination page ={page} totalPages={totalPages} onChange={setPage} />
    </main>
  )
}

export default App