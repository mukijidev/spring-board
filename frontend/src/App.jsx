import { useEffect, useState } from 'react'
import Pagination from './Pagination'
import SearchBar from './SearchBar'
import './App.css'


const PAGE_SIZE = 3

function App() {
  const [posts, setPosts] = useState([])
  const [page, setPage] = useState(0)
  const [totalPages, setTotalPages] = useState(0)
  const [keyword, setKeyword] = useState('')


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
      {posts.length === 0 && <p>No posts found.</p>}
      <ul>
        {posts.map((post) => (
          <li key={post.id}>{post.title}</li>
        ))}
      </ul>
      <Pagination page ={page} totalPages={totalPages} onChange={setPage} />
    </main>
  )
}

export default App