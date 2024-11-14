'use strict';

const main = {
    init: function () {
        const _this = this;

        // 게시글 등록 폼 제출 이벤트
        const postForm = document.getElementById('postForm');
        if (postForm) {
            postForm.addEventListener('submit', function (event) {
                event.preventDefault();
                _this.save();
            });
        }

        // 페이지 로드시 게시글 목록 불러오기
        this.loadPosts();
    },

    save: function () {
        const data = {
            title: document.getElementById('title').value,
            author: document.getElementById('author').value,
            content: document.getElementById('content').value
        };

        fetch('/api/v1/posts', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                alert('글이 등록되었습니다.');
                window.location.reload();
            })
            .catch(error => {
                alert('글 등록에 실패했습니다.');
                console.error('Error:', error);
            });
    },

    loadPosts: function () {
        const postsTable = document.getElementById('posts');
        if (!postsTable) return;

        fetch('/api/v1/posts/list')
            .then(response => response.json())
            .then(posts => {
                let html = '';
                posts.forEach(post => {
                    html += `
                        <tr>
                            <td>${post.id}</td>
                            <td>${post.title}</td>
                            <td>${post.author}</td>
                            <td>${new Date(post.modifiedDate).toLocaleString()}</td>
                            <td>
                                <button onclick="main.modifyPost(${post.id})" class="btn btn-primary btn-sm">수정</button>
                                <button onclick="main.deletePost(${post.id})" class="btn btn-danger btn-sm">삭제</button>
                            </td>
                        </tr>
                    `;
                });
                postsTable.innerHTML = html;
            })
            .catch(error => console.error('Error:', error));
    },

    modifyPost: function (id) {
        const newTitle = prompt('새로운 제목을 입력하세요:');
        const newContent = prompt('새로운 내용을 입력하세요:');

        if (newTitle === null || newContent === null) return;

        const data = {
            title: newTitle,
            content: newContent
        };

        fetch(`/api/v1/posts/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                alert('글이 수정되었습니다.');
                this.loadPosts();
            })
            .catch(error => {
                alert('글 수정에 실패했습니다.');
                console.error('Error:', error);
            });
    },

    deletePost: function (id) {
        if (!confirm('정말로 삭제하시겠습니까?')) return;

        fetch(`/api/v1/posts/${id}`, {
            method: 'DELETE',
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                alert('글이 삭제되었습니다.');
                this.loadPosts();
            })
            .catch(error => {
                alert('글 삭제에 실패했습니다.');
                console.error('Error:', error);
            });
    }
};

main.init();
